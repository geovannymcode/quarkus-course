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

    public Optional<Bookmark> findByUrl(String url) {
        return find("url", url).firstResultOptional();
    }

    public PanacheQuery<Bookmark> findAllDesc() {
        return findAll(Sort.descending("id"));
    }

    public PagedResult<Bookmark> findByPage(int pageNo, int size) {
        int idx = pageNo > 1 ? pageNo - 1 : 0;
        Page page = Page.of(idx, size);

        PanacheQuery<Bookmark> query = findAllDesc().page(page);

        return new PagedResult<>(
                query.list(),
                idx + 1,
                query.pageCount(),
                query.count(),
                query.hasNextPage(),
                query.hasPreviousPage()
        );
    }

    public int updateFields(Long id, Bookmark b) {
        return update("title=?1, url=?2, description=?3 where id=?4",
                b.title, b.url, b.description, id);
    }
}
