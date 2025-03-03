package com.example.store.product.infrastructure.repository.implementation;

import com.example.store.product.infrastructure.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String category);

    boolean existsByName(String name);
}
