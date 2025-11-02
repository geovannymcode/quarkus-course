package com.geovannycode.bookmarker.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Datos para registro de nuevo usuario")
public record RegisterRequest(
        @Schema(description = "Nombre de usuario", example = "geovannycode", required = true)
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Schema(description = "Email", example = "contact@geovannycode.com", required = true)
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @Schema(description = "Contraseña", example = "SecurePass123!", required = true)
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @Schema(description = "Nombre completo", example = "Geovanny Mendoza")
        String fullName
) {}
