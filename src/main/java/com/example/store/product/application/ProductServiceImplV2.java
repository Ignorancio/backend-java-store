package com.example.store.product.application;

import com.example.store.product.domain.Product;
import com.example.store.product.infrastructure.repository.implementation.ProductRepositoryImplV2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImplV2 {

    private final ProductRepositoryImplV2 productRepositoryImplV2;

    public Page<Product> findAll(Pageable pageable) {
        return productRepositoryImplV2.findAll(pageable);
    }
}
