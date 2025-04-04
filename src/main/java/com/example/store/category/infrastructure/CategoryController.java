package com.example.store.category.infrastructure;

import com.example.store.category.domain.Category;
import com.example.store.category.infrastructure.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryController {

    ResponseEntity<Category> save(CategoryDTO category);

    ResponseEntity<List<Category>> findAll();

    ResponseEntity<Category> findById(Long id);

    ResponseEntity<Category> update(Long id,CategoryDTO category);

    ResponseEntity<Void> delete(Long id);
}
