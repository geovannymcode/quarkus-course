package com.geovannycode.bookmarker.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Name is required and cannot be blank")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Slug is required and cannot be blank")
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Slug must be lowercase alphanumeric with hyphens (e.g., 'spring-boot')")
        @Size(min = 2, max = 100, message = "Slug must be between 2 and 100 characters")
        String slug
) {
    public CategoryRequest {
        name = name != null ? name.trim() : null;
        slug = slug != null ? slug.trim().toLowerCase() : null;
    }
}
