package com.geovannycode.bookmarker.models;

import com.geovannycode.bookmarker.entities.Bookmark;

import java.time.LocalDateTime;

public record BookmarkResponse(
        Long id,
        String title,
        String url,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static BookmarkResponse fromEntity(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.id,
                bookmark.title,
                bookmark.url,
                bookmark.description,
                bookmark.createdAt,
                bookmark.updatedAt
        );
    }
}

