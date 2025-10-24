package com.geovannycode.bookmarker.services;

import com.geovannycode.bookmarker.entities.Bookmark;
import com.geovannycode.bookmarker.models.PagedResult;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class BookmarkServiceTest {

    @Inject
    BookmarkService bookmarkService;

    @Test
    void testGetAllBookmarks() {
        var bookmarks = bookmarkService.getAllBookmarks();
        assertFalse(bookmarks.isEmpty(), "La lista de bookmarks no debe estar vacía");
    }

    @Test
    void testGetBookmarksByPage() {
        PagedResult<Bookmark> paged = bookmarkService.getBookmarks(1);
        assertFalse(paged.data().isEmpty(), "Debe devolver resultados paginados");
        assertEquals(1, paged.currentPageNo(), "La página debe ser la 1");
    }

    @Test
    void testGetBookmarkById() {
        Optional<Bookmark> bookmark = bookmarkService.getBookmarkById(1L);
        assertTrue(bookmark.isPresent(), "Debe existir un bookmark con id=1");
        assertNotNull(bookmark.get().title, "El título no debe ser nulo");
    }

    @Test
    void testSaveBookmark() {
        Bookmark bookmark = new Bookmark();
        bookmark.title = "Nuevo bookmark";
        bookmark.url = "https://quarkus.io";
        bookmark.description = "Bookmark de prueba";

        Bookmark saved = bookmarkService.saveBookmark(bookmark);

        assertNotNull(saved.id, "Debe generar un ID al persistir");
        assertEquals("Nuevo bookmark", saved.title);
    }

    @Test
    void testUpdateBookmark() {
        Bookmark bookmark = new Bookmark();
        bookmark.title = "Original";
        bookmark.url = "https://example.com";
        bookmark.description = "Descripción original";
        Bookmark saved = bookmarkService.saveBookmark(bookmark);

        saved.title = "Actualizado";
        saved.description = "Modificado";
        Bookmark updated = bookmarkService.updateBookmark(saved.id, saved);

        assertEquals("Actualizado", updated.title);
        assertEquals("Modificado", updated.description);
    }

    @Test
    void testDeleteBookmark() {
        Bookmark bookmark = new Bookmark();
        bookmark.title = "Para eliminar";
        bookmark.url = "https://delete.me";
        bookmark.description = "Este se eliminará";
        Bookmark saved = bookmarkService.saveBookmark(bookmark);

        assertNotNull(saved.id);

        bookmarkService.deleteBookmark(saved.id);

        Optional<Bookmark> deleted = bookmarkService.getBookmarkById(saved.id);
        assertTrue(deleted.isEmpty(), "El bookmark debe haberse eliminado");
    }
}
