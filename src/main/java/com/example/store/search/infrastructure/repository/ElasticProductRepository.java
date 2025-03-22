package com.example.store.search.infrastructure.repository;

import com.example.store.search.infrastructure.entity.ProductElasticEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticProductRepository extends ElasticsearchRepository<ProductElasticEntity,Long> {
    List<ProductElasticEntity> findByNameContainingIgnoreCase(String name);
}
