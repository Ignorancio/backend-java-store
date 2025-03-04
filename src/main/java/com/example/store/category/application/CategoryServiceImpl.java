package com.example.store.category.application;

import com.example.store.category.domain.Category;
import com.example.store.category.domain.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    public Category save(Category category) {
        return null;
    }

    public List<Category> findAll() {
        return List.of();
    }

    public Category findById(Long id) {
        return null;
    }

    public Category update(Category category) {
        return null;
    }

    public void deleteById(Long id) {

    }
}
