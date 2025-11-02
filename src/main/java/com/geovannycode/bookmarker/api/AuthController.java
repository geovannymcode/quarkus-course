package com.geovannycode.bookmarker.api;

import com.geovannycode.bookmarker.models.AuthRequest;
import com.geovannycode.bookmarker.models.AuthResponse;
import com.geovannycode.bookmarker.models.RegisterRequest;
import com.geovannycode.bookmarker.models.UserResponse;
import com.geovannycode.bookmarker.services.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Autenticación", description = "Endpoints de autenticación y registro")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @PermitAll
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y retorna un JWT token"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Credenciales inválidas"
            )
    })
    public Response login(@Valid AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return Response.ok(response).build();
    }

    @POST
    @Path("/register")
    @PermitAll
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea una nueva cuenta de usuario"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Datos inválidos o usuario ya existe"
            )
    })
    public Response register(@Valid RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"USER", "ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Obtener usuario actual",
            description = "Retorna información del usuario autenticado"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Información del usuario",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "No autenticado"
            )
    })
    public Response getCurrentUser(@Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        return Response.ok(new UserInfo(username)).build();
    }

    public record UserInfo(String username) {}
}
