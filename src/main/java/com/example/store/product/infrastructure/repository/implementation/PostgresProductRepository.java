package com.example.store.product.infrastructure.repository.implementation;

import com.example.store.product.domain.Product;
import com.example.store.product.domain.ProductRepository;
import com.example.store.product.infrastructure.entity.ProductEntity;
import com.example.store.product.infrastructure.mapper.ProductMapper;
import com.example.store.product.infrastructure.repository.QueryProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("postgresProductRepository")
@AllArgsConstructor
public class PostgresProductRepository implements ProductRepository {

    QueryProductRepository queryProductRepository;
    ProductMapper productMapper;

    public Product save(Product product) {
        ProductEntity saved = queryProductRepository.save(productMapper.productToProductEntity(product));
        return productMapper.productEntityToProduct(saved);
    }

    public Optional<Product> findById(Long id) {
        return queryProductRepository.findById(id).map(productMapper::productEntityToProduct);
    }

    public void deleteById(Long id) {
        queryProductRepository.deleteById(id);
    }

    public List<Product> findAll() {
        return queryProductRepository.findAll().stream().map(productMapper::productEntityToProduct).toList();
    }
}
