package com.example.store.product.infrastructure.repository.implementation;

import com.example.store.product.infrastructure.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
}
