package com.example.store.category.infrastructure.repository.implementation;

import com.example.store.category.domain.Category;
import com.example.store.category.domain.CategoryRepository;
import com.example.store.category.infrastructure.entity.CategoryEntity;
import com.example.store.category.infrastructure.mapper.CategoryMapper;
import com.example.store.category.infrastructure.repository.QueryCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostgresCategoryRepository implements CategoryRepository {

    private final QueryCategoryRepository queryCategoryRepository;
    private final CategoryMapper categoryMapper;

    public Category save(Category category) {
        CategoryEntity entity = categoryMapper.categoryToCategoryEntity(category);
        CategoryEntity savedEntity = queryCategoryRepository.save(entity);
        return categoryMapper.categoryEntityToCategory(savedEntity);
    }

    public Optional<Category> findById(Long id) {
        return queryCategoryRepository.findById(id).map(categoryMapper::categoryEntityToCategory);
    }

    public List<Category> findAll() {
        return queryCategoryRepository.findAll().stream().map(categoryMapper::categoryEntityToCategory).toList();
    }

    public Boolean existsByName(String name) {
        return queryCategoryRepository.existsByName(name);
    }

    public void deleteById(Long id) {
        queryCategoryRepository.deleteById(id);
    }

    public Optional<Category> findByName(String name){
        return queryCategoryRepository.findByName(name).map(categoryMapper::categoryEntityToCategory);
    }
}
