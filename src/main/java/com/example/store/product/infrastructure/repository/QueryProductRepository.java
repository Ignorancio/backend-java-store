package com.example.store.product.infrastructure.repository;

import com.example.store.product.infrastructure.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
