package com.geovannycode.bookmarker.repository;

import com.geovannycode.bookmarker.entities.Category;
import com.geovannycode.bookmarker.models.PagedResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;


@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {

    public Optional<Category> findBySlug(String slug) {
        return find("slug", slug).firstResultOptional();
    }

    public PagedResult<Category> findByPage(int pageNo, int size) {
        pageNo = pageNo > 1 ? pageNo - 1 : 0;
        Page page = Page.of(pageNo, size);
        PanacheQuery<Category> query = findAll().page(page);

        return new PagedResult<>(
                query.list(),
                pageNo + 1,
                query.pageCount(),
                query.count(),
                query.hasNextPage(),
                query.hasPreviousPage());
    }
}
