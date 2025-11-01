package com.geovannycode.bookmarker.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


public record BookmarkRequest(
        @NotEmpty(message = "Title is required")
        String title,

        @NotEmpty(message = "URL is required")
        @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
        String url,

        String description
) {

    public BookmarkRequest {
        title = title != null ? title.trim() : null;
        url = url != null ? url.trim() : null;
        description = description != null ? description.trim() : null;
    }
}

