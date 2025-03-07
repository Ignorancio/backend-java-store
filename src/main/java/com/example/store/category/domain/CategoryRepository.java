package com.example.store.category.domain;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    Boolean existsByName(String name);

    void deleteById(Long id);

    Optional<Category> findByName(String name);
}
