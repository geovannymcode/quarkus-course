package com.geovannycode.bookmarker.services;

import com.geovannycode.bookmarker.ApplicationProperties;
import com.geovannycode.bookmarker.entities.Bookmark;
import com.geovannycode.bookmarker.exceptions.BadRequestException;
import com.geovannycode.bookmarker.exceptions.ResourceNotFoundException;
import com.geovannycode.bookmarker.models.PagedResult;
import com.geovannycode.bookmarker.repository.BookmarkRepository;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookmarkService {
    private final ApplicationProperties properties;
    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(ApplicationProperties properties, BookmarkRepository bookmarkRepository) {
        this.properties = properties;
        this.bookmarkRepository = bookmarkRepository;
    }

    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAllDesc().list();
    }

    public PagedResult<Bookmark> getBookmarks(int page) {
        return bookmarkRepository.findByPage(page, properties.pageSize());
    }

    @CacheResult(cacheName = "bookmarks-cache")
    public Optional<Bookmark> getBookmarkById(@CacheKey Long id) {
        return bookmarkRepository.findByIdOptional(id);
    }

    @Transactional
    public Bookmark saveBookmark(Bookmark bookmark) {
        bookmark.id = null;
        bookmarkRepository.persist(bookmark);
        return bookmark;
    }

    @Transactional
    @CacheInvalidate(cacheName = "bookmarks-cache")
    public Bookmark updateBookmark(@CacheKey Long id, Bookmark bookmark) {
        int updated = bookmarkRepository.updateFields(id, bookmark);
        if (updated == 0) {
            throw new BadRequestException("Bookmark not found with id: " + id);
        }
        return bookmark;
    }

    @Transactional
    @CacheInvalidate(cacheName = "bookmarks-cache")
    public void deleteBookmark(@CacheKey Long id) {
        Bookmark entity = bookmarkRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found with id: " + id));
        bookmarkRepository.delete(entity);
    }
}
