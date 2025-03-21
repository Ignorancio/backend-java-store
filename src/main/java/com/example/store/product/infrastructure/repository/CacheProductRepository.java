package com.example.store.product.infrastructure.repository;

import com.example.store.product.infrastructure.entity.ProductCacheEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CacheProductRepository extends CrudRepository<ProductCacheEntity, Long> {
    @NonNull
    List<ProductCacheEntity> findAll();
}
