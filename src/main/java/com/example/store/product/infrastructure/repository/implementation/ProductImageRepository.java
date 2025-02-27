package com.example.store.product.infrastructure.repository.implementation;

import com.example.store.product.infrastructure.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
