package com.example.store.product.infrastructure;


import com.example.store.product.application.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProduct(id);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestPart("product") ProductRequest product,
                                                         @RequestPart(value = "file",required = false) MultipartFile file) {
        ProductResponse productSaved = productService.createProduct(product, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
    }
    @PutMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestPart("product") ProductUpdateRequest product,
                                                         @RequestPart(value = "file",required = false) MultipartFile file) {
        ProductResponse productSaved = productService.updateProduct(product,file);
        if(productSaved == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productSaved);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id) {
        ProductResponse product = productService.deleteProduct(id);
        return ResponseEntity.ok(product);
    }

}
