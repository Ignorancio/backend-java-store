package com.example.store.product.infrastructure.repository.implementation;

import com.example.store.product.infrastructure.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String category);

    boolean existsByName(String name);
}
