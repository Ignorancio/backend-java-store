package com.example.store.product.infrastructure.repository;

import com.example.store.product.infrastructure.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
}
