package com.example.store.product.infrastructure.repository.implementation;

import com.example.store.product.domain.Product;
import com.example.store.product.infrastructure.mapper.ProductMapper;
import com.example.store.product.infrastructure.repository.QueryProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImplV2 {

    private final QueryProductRepository queryProductRepository;
    private final ProductMapper productMapper;


    public Page<Product> findAll(Pageable pageable) {
        return queryProductRepository.findAll(pageable).map(productMapper::productEntityToProduct);
    }
}
