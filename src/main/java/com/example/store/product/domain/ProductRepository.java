package com.example.store.product.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    void deleteById(Long id);

    List<Product> findAll();

    List<Product> findAllById(List<Long> ids);
}
