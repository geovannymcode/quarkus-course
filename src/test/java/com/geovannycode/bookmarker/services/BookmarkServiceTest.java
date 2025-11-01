package com.geovannycode.bookmarker.services;

import com.geovannycode.bookmarker.entities.Bookmark;
import com.geovannycode.bookmarker.exceptions.ResourceNotFoundException;
import com.geovannycode.bookmarker.models.BookmarkRequest;
import com.geovannycode.bookmarker.models.PagedResult;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class BookmarkServiceTest {

    @Inject
    BookmarkService bookmarkService;

    private BookmarkRequest testRequest;

    @BeforeEach
    void setUp() {
        testRequest = new BookmarkRequest(
                "Test Bookmark",
                "https://test-example.com",
                "Test description"
        );
    }

    @Test
    void testGetAllBookmarks() {
        List<Bookmark> bookmarks = bookmarkService.getAllBookmarks();
        assertNotNull(bookmarks, "La lista de bookmarks no debe ser nula");
        assertFalse(bookmarks.isEmpty(), "La lista de bookmarks no debe estar vacía");
    }

    @Test
    void testGetBookmarksByPage() {
        PagedResult<Bookmark> paged = bookmarkService.getBookmarks(1);

        assertNotNull(paged, "El resultado paginado no debe ser nulo");
        assertNotNull(paged.data(), "Los datos paginados no deben ser nulos");
        assertFalse(paged.data().isEmpty(), "Debe devolver resultados paginados");
        assertEquals(1, paged.currentPageNo(), "La página debe ser la 1");
        assertTrue(paged.totalPages() > 0, "Debe haber al menos una página");
    }

    @Test
    void testGetBookmarkById_WhenExists() {
        // Primero crear un bookmark para asegurar que existe
        Bookmark created = createTestBookmark("https://test-get-by-id.com");

        // Buscar el bookmark creado
        Bookmark bookmark = bookmarkService.getBookmarkById(created.getId());

        assertNotNull(bookmark, "El bookmark debe existir");
        assertEquals(created.getId(), bookmark.getId());
        assertNotNull(bookmark.getTitle(), "El título no debe ser nulo");
    }

    @Test
    void testGetBookmarkById_WhenNotExists_ShouldThrowException() {
        Long nonExistentId = 999999L;

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookmarkService.getBookmarkById(nonExistentId),
                "Debe lanzar ResourceNotFoundException cuando el bookmark no existe"
        );
    }

    @Test
    @Transactional
    void testCreateBookmark() {
        BookmarkRequest request = new BookmarkRequest(
                "Nuevo bookmark",
                "https://unique-url-" + System.currentTimeMillis() + ".com",
                "Bookmark de prueba"
        );

        Bookmark saved = bookmarkService.createBookmark(request);

        assertNotNull(saved, "El bookmark guardado no debe ser nulo");
        assertNotNull(saved.getId(), "Debe generar un ID al persistir");
        assertEquals(request.title(), saved.getTitle());
        assertEquals(request.url(), saved.getUrl());
        assertEquals(request.description(), saved.getDescription());
        assertNotNull(saved.getCreatedAt(), "Debe tener fecha de creación");
        assertNotNull(saved.getUpdatedAt(), "Debe tener fecha de actualización");
    }

    @Test
    @Transactional
    void testCreateBookmark_WithDuplicateUrl_ShouldThrowException() {
        String duplicateUrl = "https://duplicate-" + System.currentTimeMillis() + ".com";

        // Crear primer bookmark
        BookmarkRequest firstRequest = new BookmarkRequest(
                "First Bookmark",
                duplicateUrl,
                "First description"
        );
        bookmarkService.createBookmark(firstRequest);

        // Intentar crear segundo bookmark con misma URL
        BookmarkRequest duplicateRequest = new BookmarkRequest(
                "Duplicate Bookmark",
                duplicateUrl,
                "Duplicate description"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> bookmarkService.createBookmark(duplicateRequest),
                "Debe lanzar excepción al intentar crear bookmark con URL duplicada"
        );
    }

    @Test
    @Transactional
    void testUpdateBookmark() {
        // Crear bookmark inicial
        Bookmark created = createTestBookmark("https://test-update-" + System.currentTimeMillis() + ".com");
        Long bookmarkId = created.getId();

        // Preparar actualización
        BookmarkRequest updateRequest = new BookmarkRequest(
                "Título Actualizado",
                "https://updated-" + System.currentTimeMillis() + ".com",
                "Descripción actualizada"
        );

        // Actualizar
        Bookmark updated = bookmarkService.updateBookmark(bookmarkId, updateRequest);

        assertNotNull(updated, "El bookmark actualizado no debe ser nulo");
        assertEquals(bookmarkId, updated.getId(), "El ID debe permanecer igual");
        assertEquals(updateRequest.title(), updated.getTitle());
        assertEquals(updateRequest.url(), updated.getUrl());
        assertEquals(updateRequest.description(), updated.getDescription());
    }

    @Test
    @Transactional
    void testUpdateBookmark_WhenNotExists_ShouldThrowException() {
        Long nonExistentId = 999999L;
        BookmarkRequest updateRequest = new BookmarkRequest(
                "Update Title",
                "https://update.com",
                "Update description"
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookmarkService.updateBookmark(nonExistentId, updateRequest),
                "Debe lanzar ResourceNotFoundException al actualizar bookmark inexistente"
        );
    }

    @Test
    @Transactional
    void testDeleteBookmark() {
        // Crear bookmark para eliminar
        Bookmark created = createTestBookmark("https://test-delete-" + System.currentTimeMillis() + ".com");
        Long bookmarkId = created.getId();

        // Verificar que existe
        assertNotNull(bookmarkService.getBookmarkById(bookmarkId));

        // Eliminar
        bookmarkService.deleteBookmark(bookmarkId);

        // Verificar que ya no existe
        assertThrows(
                ResourceNotFoundException.class,
                () -> bookmarkService.getBookmarkById(bookmarkId),
                "El bookmark debe haberse eliminado"
        );
    }

    @Test
    @Transactional
    void testDeleteBookmark_WhenNotExists_ShouldThrowException() {
        Long nonExistentId = 999999L;

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookmarkService.deleteBookmark(nonExistentId),
                "Debe lanzar ResourceNotFoundException al eliminar bookmark inexistente"
        );
    }

    @Test
    void testGetBookmarksByPage_WithDifferentPages() {
        PagedResult<Bookmark> page1 = bookmarkService.getBookmarks(1);
        PagedResult<Bookmark> page2 = bookmarkService.getBookmarks(2);

        assertNotNull(page1);
        assertNotNull(page2);
        assertEquals(1, page1.currentPageNo());
        assertEquals(2, page2.currentPageNo());
    }

    @Test
    @Transactional
    void testCreateBookmark_WithMinimalData() {
        BookmarkRequest minimalRequest = new BookmarkRequest(
                "Minimal",
                "https://minimal-" + System.currentTimeMillis() + ".com",
                null // descripción opcional
        );

        Bookmark saved = bookmarkService.createBookmark(minimalRequest);

        assertNotNull(saved);
        assertEquals(minimalRequest.title(), saved.getTitle());
        assertEquals(minimalRequest.url(), saved.getUrl());
        assertNull(saved.getDescription());
    }

    /**
     * Helper method to create a test bookmark with unique URL.
     */
    @Transactional
    private Bookmark createTestBookmark(String url) {
        BookmarkRequest request = new BookmarkRequest(
                "Test Bookmark",
                url,
                "Test description"
        );
        return bookmarkService.createBookmark(request);
    }
}
