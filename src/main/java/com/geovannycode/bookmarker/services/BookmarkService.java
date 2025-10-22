package com.geovannycode.bookmarker.services;

import com.geovannycode.bookmarker.ApplicationProperties;
import com.geovannycode.bookmarker.entities.Bookmark;
import com.geovannycode.bookmarker.exceptions.BadRequestException;
import com.geovannycode.bookmarker.exceptions.ResourceNotFoundException;
import com.geovannycode.bookmarker.models.PagedResult;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookmarkService {
    private final ApplicationProperties properties;

    public BookmarkService(ApplicationProperties properties) {
        this.properties = properties;
    }

    public List<Bookmark> getAllBookmarks() {
        return Bookmark.listAll(Sort.descending("id"));
    }

    public PagedResult<Bookmark> getBookmarks(int page) {
        PanacheQuery<Bookmark> pageResult =
                Bookmark.find("select b from Bookmark b order by b.id desc")
                        .page(page - 1, properties.pageSize());
        return new PagedResult<>(
                pageResult.list(),
                page,
                pageResult.pageCount(),
                pageResult.count(),
                pageResult.hasNextPage(),
                pageResult.hasPreviousPage());
    }

    @CacheResult(cacheName = "bookmarks-cache")
    public Optional<Bookmark> getBookmarkById(@CacheKey Long id) {
        return Bookmark.findByIdOptional(id);
    }

    @Transactional
    public Bookmark saveBookmark(Bookmark bookmark) {
        bookmark.id = null;
        bookmark.persist();
        return bookmark;
    }

    @Transactional
    @CacheInvalidate(cacheName = "bookmarks-cache")
    public Bookmark updateBookmark(@CacheKey Long id, Bookmark bookmark) {
        int updateCount = Bookmark.update("title=?1, url=?2, description=?3 where id=?4",
                bookmark.title, bookmark.url, bookmark.description, id);
        if (updateCount == 0) {
            throw new BadRequestException("Bookmark not found with id: " + id);
        }
        return bookmark;
    }

    @Transactional
    @CacheInvalidate(cacheName = "bookmarks-cache")
    public void deleteBookmark(@CacheKey Long id) {
        Bookmark.findByIdOptional(id)
                .orElseThrow(()->new ResourceNotFoundException("Bookmark not found with id: " + id))
                .delete();
    }
}
