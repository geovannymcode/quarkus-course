package com.geovannycode.bookmarker.models;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Credenciales de autenticación")
public record AuthRequest(
        @Schema(description = "Nombre de usuario", example = "geovannycode", required = true)
        @NotBlank(message = "Username is required")
        String username,

        @Schema(description = "Contraseña", example = "SecurePass123!", required = true)
        @NotBlank(message = "Password is required")
        String password
) {}
