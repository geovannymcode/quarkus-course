package com.geovannycode.bookmarker.models;

import com.geovannycode.bookmarker.entities.User;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Información del usuario")
public record UserResponse(
        @Schema(description = "ID del usuario", example = "1")
        Long id,

        @Schema(description = "Nombre de usuario", example = "geovannycode")
        String username,

        @Schema(description = "Email", example = "contact@geovannycode.com")
        String email,

        @Schema(description = "Nombre completo", example = "Geovanny Mendoza")
        String fullName,

        @Schema(description = "Roles", example = "[\"USER\", \"ADMIN\"]")
        Set<String> roles
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles()
        );
    }
}
