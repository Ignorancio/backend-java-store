package com.example.store.product.infrastructure.repository.implementation;

import com.example.store.product.domain.ProductImage;
import com.example.store.product.domain.ProductImageRepository;
import com.example.store.product.infrastructure.entity.ProductImageEntity;
import com.example.store.product.infrastructure.mapper.ProductImageMapper;
import com.example.store.product.infrastructure.repository.QueryProductImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class PostgresProductImageRepository implements ProductImageRepository {

    private final QueryProductImageRepository queryProductImageRepository;
    private final ProductImageMapper productImageMapper;

    public ProductImage save(ProductImage productImage) {
        ProductImageEntity savedEntity = queryProductImageRepository.save(productImageMapper.productImageToProductImageEntity(productImage));
        return productImageMapper.productImageEntityToProductImage(savedEntity);
    }
}
