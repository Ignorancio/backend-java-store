package com.example.store.category.infrastructure.repository.implementation;

import com.example.store.category.domain.Category;
import com.example.store.category.domain.CategoryRepository;

import java.util.List;

public class PostgresCategoryRepository implements CategoryRepository {
    public Category save(Category category) {
        return null;
    }

    public Category findById(Long id) {
        return null;
    }

    public List<Category> findAll() {
        return List.of();
    }

    public Boolean existsByName(String name) {
        return null;
    }

    public void delete(Category category) {

    }
}
