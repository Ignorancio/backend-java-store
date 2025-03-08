package com.example.store.product.infrastructure;

import com.example.store.product.domain.Product;
import com.example.store.product.infrastructure.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductController {

    ResponseEntity<Product> save(ProductDTO product, MultipartFile file);

    ResponseEntity<Product> findById(Long id);

    ResponseEntity<List<Product>> findAll();

    ResponseEntity<Product> update(Product product, Optional<MultipartFile> file);

    ResponseEntity<Void> delete(Long id);
}
