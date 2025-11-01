package com.geovannycode.bookmarker.services;

import com.geovannycode.bookmarker.ApplicationProperties;
import com.geovannycode.bookmarker.entities.Bookmark;
import com.geovannycode.bookmarker.exceptions.ResourceNotFoundException;
import com.geovannycode.bookmarker.models.BookmarkRequest;
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

    private static final String BOOKMARKS_CACHE = "bookmarks-cache";
    private static final String BOOKMARK_NOT_FOUND_MESSAGE = "Bookmark not found with id: %d";

    private final ApplicationProperties properties;
    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(ApplicationProperties properties, BookmarkRepository bookmarkRepository) {
        this.properties = properties;
        this.bookmarkRepository = bookmarkRepository;
    }


    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAllDescending().list();
    }

    public PagedResult<Bookmark> getBookmarks(int page) {
        int pageSize = properties.pageSize();
        return bookmarkRepository.findByPage(page, pageSize);
    }

    @CacheResult(cacheName = BOOKMARKS_CACHE)
    public Bookmark getBookmarkById(@CacheKey Long id) {
        return findBookmarkById(id);
    }

    @Transactional
    @CacheInvalidate(cacheName = BOOKMARKS_CACHE)
    public Bookmark createBookmark(BookmarkRequest request) {
        validateBookmarkUrl(request.url());

        var bookmark = Bookmark.builder()
                .title(request.title())
                .url(request.url())
                .description(request.description())
                .build();

        bookmarkRepository.persist(bookmark);
        return bookmark;
    }

    @Transactional
    @CacheInvalidate(cacheName = BOOKMARKS_CACHE)
    public Bookmark updateBookmark(@CacheKey Long id, BookmarkRequest request) {
        Bookmark existingBookmark = findBookmarkById(id);
        validateBookmarkUrl(request.url());

        int updatedRows = bookmarkRepository.updateFields(
                id,
                request.title(),
                request.url(),
                request.description()
        );

        if (updatedRows == 0) {
            throw new ResourceNotFoundException(
                    String.format(BOOKMARK_NOT_FOUND_MESSAGE, id)
            );
        }

        // Return updated entity
        return bookmarkRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(BOOKMARK_NOT_FOUND_MESSAGE, id)
                ));
    }

    @Transactional
    @CacheInvalidate(cacheName = BOOKMARKS_CACHE)
    public void deleteBookmark(@CacheKey Long id) {
        Bookmark bookmark = findBookmarkById(id);
        bookmarkRepository.delete(bookmark);
    }

    private Bookmark findBookmarkById(Long id) {
        return bookmarkRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(BOOKMARK_NOT_FOUND_MESSAGE, id)
                ));
    }

    private void validateBookmarkUrl(String url) {
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUrl(url);
        if (existingBookmark.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("A bookmark with URL '%s' already exists", url)
            );
        }
    }
}
