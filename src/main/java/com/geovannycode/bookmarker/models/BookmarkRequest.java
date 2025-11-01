package com.geovannycode.bookmarker.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Datos para crear o actualizar un bookmark")
public record BookmarkRequest(
        @Schema(description = "Título del bookmark", example = "Spring Boot Documentation", required = true)
        @NotEmpty(message = "Title is required")
        String title,

        @Schema(description = "URL del bookmark", example = "https://spring.io/projects/spring-boot", required = true)
        @NotEmpty(message = "URL is required")
        @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
        String url,

        @Schema(description = "Descripción del bookmark", example = "Official Spring Boot documentation")
        String description
) {

    public BookmarkRequest {
        title = title != null ? title.trim() : null;
        url = url != null ? url.trim() : null;
        description = description != null ? description.trim() : null;
    }
}

