package com.example.store.product.domain;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product save(Product product, MultipartFile file);

    List<Product> findAll();

    Product findById(Long id);

    Product update(Product product, Optional<MultipartFile> file);

    void deleteById(Long id);
}
