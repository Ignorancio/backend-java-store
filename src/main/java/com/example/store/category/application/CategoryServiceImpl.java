package com.example.store.category.application;

import com.example.store.category.domain.Category;
import com.example.store.category.domain.CategoryRepository;
import com.example.store.category.domain.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        if(categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Categoria ya existe");
        }
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
    }

    public Category update(Category category) {
        Category categorySearch = categoryRepository.findById(category.getId()).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        categoryRepository.deleteById(id);
    }
}
