package com.example.store.category.application;

import com.example.store.category.domain.Category;

import java.util.List;

public interface CategoryService {

    Category save(Category category);

    List<Category> findAll();

    Category findById(Long id);

    Category update(Category category);

    void deleteById(Long id);
}
