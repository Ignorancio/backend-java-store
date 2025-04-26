package com.example.store.product.infrastructure.repository.implementation;

import com.example.store.product.domain.Product;
import com.example.store.product.domain.ProductRepository;
import com.example.store.product.infrastructure.entity.ProductEntity;
import com.example.store.product.infrastructure.mapper.ProductMapper;
import com.example.store.product.infrastructure.repository.CacheProductRepository;
import com.example.store.product.infrastructure.repository.QueryProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    QueryProductRepository queryProductRepository;
    CacheProductRepository cacheProductRepository;
    ProductMapper productMapper;

    public Product save(Product product) {
        ProductEntity saved = queryProductRepository.save(productMapper.productToProductEntity(product));
        Product productSaved = productMapper.productEntityToProduct(saved);
        cacheProductRepository.save(productMapper.productToProductCacheEntity(productSaved));
        return productSaved;
    }

    public Optional<Product> findById(Long id) {
        Optional<Product> product = cacheProductRepository.findById(id).map(productMapper::productCacheEntityToProduct);
        if(product.isEmpty()){
            product = queryProductRepository.findById(id).map(productMapper::productEntityToProduct);
            product.ifPresent(productSaved ->
                    cacheProductRepository.save(productMapper.productToProductCacheEntity(productSaved)));
        }
        return product;
    }

    public void deleteById(Long id) {
        queryProductRepository.deleteById(id);
        cacheProductRepository.deleteById(id);
    }

    public List<Product> findAll() {
        return queryProductRepository.findAll().stream().map(productMapper::productEntityToProduct).toList();
    }

    public List<Product> findAllById(List<Long> ids) {
        return queryProductRepository.findAllById(ids).stream().map(productMapper::productEntityToProduct).toList();
    }
}
