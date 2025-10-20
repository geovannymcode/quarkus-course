package com.geovannycode.bookmarker.models;

import java.util.List;

public record PagedResult<T>(
        List<T> data,
        int currentPageNo,
        int totalPages,
        long totalElements,
        boolean hasNextPage,
        boolean hasPreviousPage) {
}
