package com.geovannycode.bookmarker.services;

import com.geovannycode.bookmarker.entities.Category;
import com.geovannycode.bookmarker.models.PagedResult;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class CategoryServiceTest {
    @Inject
    CategoryService categoryService;

    @Test
    void testFindBySlug() {
        Optional<Category> category = categoryService.findBySlug("java");
        assertTrue(category.isPresent());
        assertEquals("java", category.get().getSlug());
    }

    @Test
    void testFindByPage() {
        PagedResult<Category> pagedResult = categoryService.findByPage(1);
        assertFalse(pagedResult.data().isEmpty());
    }

}
