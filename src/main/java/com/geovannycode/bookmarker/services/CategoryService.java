package com.geovannycode.bookmarker.services;

import com.geovannycode.bookmarker.ApplicationProperties;
import com.geovannycode.bookmarker.entities.Category;
import com.geovannycode.bookmarker.exceptions.ResourceNotFoundException;
import com.geovannycode.bookmarker.models.PagedResult;
import com.geovannycode.bookmarker.repository.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ApplicationProperties properties;

    public CategoryService(CategoryRepository categoryRepository,
                           ApplicationProperties properties) {
        this.categoryRepository = categoryRepository;
        this.properties = properties;
    }

    public List<Category> findAll() {
        return categoryRepository.listAll();
    }

    public PagedResult<Category> findByPage(int pageNo) {
        return categoryRepository.findByPage(pageNo, properties.pageSize());
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findByIdOptional(id);
    }

    public Optional<Category> findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Transactional
    public Category createCategory(String name, String slug) {
        Category category = new Category(name, slug);
        categoryRepository.persist(category);
        return category;
    }

    @Transactional
    public Category updateCategory(Long id, String name, String slug) {
        Category category = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setName(name);
        category.setSlug(slug);
        categoryRepository.persist(category);
        return category;
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}
