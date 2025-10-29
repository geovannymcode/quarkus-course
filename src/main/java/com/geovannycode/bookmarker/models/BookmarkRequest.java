package com.geovannycode.bookmarker.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BookmarkRequest(
        @NotBlank(message = "Title is required and cannot be blank")
        @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
        String title,

        @NotBlank(message = "URL is required and cannot be blank")
        @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
        @Size(max = 500, message = "URL cannot exceed 500 characters")
        String url,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description
) {
    public BookmarkRequest {
        title = title != null ? title.trim() : null;
        url = url != null ? url.trim() : null;
        description = description != null && !description.isBlank() ? description.trim() : null;
    }
}

