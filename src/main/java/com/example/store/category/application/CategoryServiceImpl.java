package com.example.store.category.application;

import com.example.store.category.domain.Category;
import com.example.store.category.domain.CategoryRepository;
import com.example.store.category.domain.CategoryService;
import com.example.store.category.infrastructure.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(CategoryDTO categoryDTO) {
        if(categoryRepository.existsByName(categoryDTO.name())) {
            throw new IllegalArgumentException("Categoria ya existe");
        }
        Category category = Category.builder().name(categoryDTO.name()).build();
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
        return categoryRepository.save(categorySearch);
    }

    public void deleteById(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        categoryRepository.deleteById(id);
    }
}
