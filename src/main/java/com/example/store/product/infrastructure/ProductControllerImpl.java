package com.example.store.product.infrastructure;

import com.example.store.product.domain.Product;
import com.example.store.product.domain.ProductService;
import com.example.store.product.infrastructure.dto.ProductDTO;
import com.example.store.product.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController{

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> save(@RequestPart("product") ProductDTO product,@RequestPart("file") MultipartFile file) {
        Product product1 = productMapper.productDTOToProduct(product);
        Product productSaved = productService.save(product1, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Product productSaved = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productSaved);
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(@RequestPart("product") Product product,@RequestPart(value = "file", required = false) Optional<MultipartFile> file) {
        Product productSaved = productService.update(product, file);
        return ResponseEntity.status(HttpStatus.OK).body(productSaved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
