package com.geovannycode.bookmarker.services;

import com.geovannycode.bookmarker.entities.User;
import com.geovannycode.bookmarker.models.AuthRequest;
import com.geovannycode.bookmarker.models.AuthResponse;
import com.geovannycode.bookmarker.models.RegisterRequest;
import com.geovannycode.bookmarker.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.HashSet;

@ApplicationScoped
public class AuthService {

    private final UserRepository userRepository;
    private final Duration tokenDuration;

    public AuthService(
            UserRepository userRepository,
            @ConfigProperty(name = "jwt.duration", defaultValue = "24h") Duration tokenDuration) {
        this.userRepository = userRepository;
        this.tokenDuration = tokenDuration;
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!user.isActive()) {
            throw new BadRequestException("User account is disabled");
        }

        if (!BcryptUtil.matches(request.password(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRolesAsString());
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User(
                request.username(),
                request.email(),
                BcryptUtil.bcryptHash(request.password())
        );
        user.setFullName(request.fullName());
        user.setRoles(new HashSet<>());
        user.addRole("USER");

        userRepository.persist(user);

        String token = generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRolesAsString());
    }

    private String generateToken(User user) {
        return Jwt.issuer("bookmarker-api")
                .upn(user.getUsername())
                .groups(user.getRoles())
                .expiresIn(tokenDuration)
                .sign();
    }
}
