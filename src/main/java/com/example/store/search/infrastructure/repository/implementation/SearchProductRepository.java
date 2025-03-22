package com.example.store.search.infrastructure.repository.implementation;

import com.example.store.product.domain.Product;
import com.example.store.search.infrastructure.mapper.SearchMapper;
import com.example.store.search.infrastructure.repository.ElasticProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SearchProductRepository {

    private final ElasticProductRepository repository;
    private final SearchMapper searchMapper;

    public List<Product>findByNameContaining(String name){
        return repository.findByNameContainingIgnoreCase(name).stream().map(searchMapper::ProductElasticEntityToProduct).toList();
    }

    public void save(Product product){
        repository.save(searchMapper.ProductToProductElasticEntity(product));
    }

    public void delete(Product product){
        repository.delete(searchMapper.ProductToProductElasticEntity(product));
    }
}
