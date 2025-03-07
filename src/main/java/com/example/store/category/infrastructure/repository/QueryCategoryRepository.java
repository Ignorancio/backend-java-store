package com.example.store.category.infrastructure.repository;

import com.example.store.category.infrastructure.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueryCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String category);

    boolean existsByName(String name);
}
