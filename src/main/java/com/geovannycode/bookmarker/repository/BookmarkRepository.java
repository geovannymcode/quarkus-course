package com.geovannycode.bookmarker.repository;

import com.geovannycode.bookmarker.entities.Bookmark;
import com.geovannycode.bookmarker.models.PagedResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class BookmarkRepository implements PanacheRepository<Bookmark> {

    private static final String SORT_FIELD = "id";
    private static final String URL_FIELD = "url";

    public Optional<Bookmark> findByUrl(String url) {
        return find(URL_FIELD, url).firstResultOptional();
    }

    public PanacheQuery<Bookmark> findAllDescending() {
        return findAll(Sort.descending(SORT_FIELD));
    }

    public PagedResult<Bookmark> findByPage(int pageNumber, int pageSize) {
        int pageIndex = calculatePageIndex(pageNumber);
        Page page = Page.of(pageIndex, pageSize);

        PanacheQuery<Bookmark> query = findAllDescending().page(page);

        return new PagedResult<>(
                query.list(),
                pageNumber,
                query.pageCount(),
                query.count(),
                query.hasNextPage(),
                query.hasPreviousPage()
        );
    }

    public int updateFields(Long id, String title, String url, String description) {
        String updateQuery = "title = ?1, url = ?2, description = ?3 where id = ?4";
        return update(updateQuery, title, url, description, id);
    }

    private int calculatePageIndex(int pageNumber) {
        return pageNumber > 1 ? pageNumber - 1 : 0;
    }
}
