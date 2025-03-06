package com.example.store.category.domain;

import com.example.store.category.infrastructure.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    Category save(CategoryDTO category);

    List<Category> findAll();

    Category findById(Long id);

    Category update(Category category);

    void deleteById(Long id);
}
