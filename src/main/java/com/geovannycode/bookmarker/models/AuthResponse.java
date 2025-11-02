package com.geovannycode.bookmarker.models;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación con JWT")
public record AuthResponse(
        @Schema(description = "Token JWT", example = "eyJhbGciOiJSUzI1NiIsInR5cCI...")
        String token,

        @Schema(description = "Tipo de token", example = "Bearer")
        String type,

        @Schema(description = "Nombre de usuario", example = "geovannycode")
        String username,

        @Schema(description = "Roles del usuario", example = "USER,ADMIN")
        String roles
) {
    public AuthResponse(String token, String username, String roles) {
        this(token, "Bearer", username, roles);
    }
}
