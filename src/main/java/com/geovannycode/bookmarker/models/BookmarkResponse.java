package com.geovannycode.bookmarker.models;

import com.geovannycode.bookmarker.entities.Bookmark;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta con datos de un bookmark")
public record BookmarkResponse(
        @Schema(description = "ID único del bookmark", example = "1")
        Long id,

        @Schema(description = "Título del bookmark", example = "Spring Boot Documentation")
        String title,

        @Schema(description = "URL del bookmark", example = "https://spring.io/projects/spring-boot")
        String url,

        @Schema(description = "Descripción del bookmark", example = "Official Spring Boot documentation")
        String description,

        @Schema(description = "Fecha de creación", example = "2025-10-31T10:15:30")
        LocalDateTime createdAt,

        @Schema(description = "Fecha de última actualización", example = "2025-10-31T10:15:30")
        LocalDateTime updatedAt
) {

    public static BookmarkResponse fromEntity(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.getId(),
                bookmark.getTitle(),
                bookmark.getTitle(),
                bookmark.getDescription(),
                bookmark.getCreatedAt(),
                bookmark.getUpdatedAt()
        );
    }
}

