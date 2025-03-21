package com.example.store.product.infrastructure.repository.implementation;

import com.example.store.product.domain.Product;
import com.example.store.product.domain.ProductRepository;
import com.example.store.product.infrastructure.entity.ProductCacheEntity;
import com.example.store.product.infrastructure.mapper.ProductMapper;
import com.example.store.product.infrastructure.repository.CacheProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("redisProductRepository")
@RequiredArgsConstructor
public class RedisProductRepository implements ProductRepository {

    private final CacheProductRepository cacheProductRepository;
    private final ProductMapper productMapper;

    public Product save(Product product) {
        ProductCacheEntity saved = cacheProductRepository.save(productMapper.productToProductCacheEntity(product));
        return productMapper.productCacheEntityToProduct(saved);
    }

    public Optional<Product> findById(Long id) {
        return cacheProductRepository.findById(id).map(productMapper::productCacheEntityToProduct);
    }

    public void deleteById(Long id) {
        cacheProductRepository.deleteById(id);
    }

    public List<Product> findAll() {
        return cacheProductRepository.findAll().stream().map(productMapper::productCacheEntityToProduct).toList();
    }
}
