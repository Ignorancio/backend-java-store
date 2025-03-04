package com.example.store.category.domain;

import java.util.List;

public interface CategoryRepository {
    Category save(Category category);

    Category findById(Long id);

    List<Category> findAll();

    Boolean existsByName(String name);

    void delete(Category category);
}
