package com.example.store.search.application;

import com.example.store.product.domain.Product;
import com.example.store.search.infrastructure.repository.implementation.SearchProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchServiceImpl {

    private final SearchProductRepository searchProductRepository;

    public List<Product>searchProduct(String name){
        return searchProductRepository.findByNameContaining(name);
    }
}
