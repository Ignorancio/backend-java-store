package com.example.store.product.infrastructure;

import com.example.store.product.domain.Product;
import com.example.store.product.domain.ProductService;
import com.example.store.product.infrastructure.dto.ProductDTO;
import com.example.store.product.infrastructure.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Product Services",description = "Operations related to products")
public class ProductControllerImpl implements ProductController{

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new product")
    public ResponseEntity<Product> save(@Valid @RequestPart("product") ProductDTO product, @RequestPart("file") MultipartFile file) {
        Product product1 = productMapper.productDTOToProduct(product);
        Product productSaved = productService.save(product1, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Return a product by id")
    @SecurityRequirements
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Product productSaved = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productSaved);
    }

    @GetMapping
    @Operation(summary = "Return all products")
    @SecurityRequirements
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a product")
    public ResponseEntity<Product> update(@PathVariable Long id,
                                          @RequestPart("product") ProductDTO product,
                                          @RequestPart(value = "file", required = false) Optional<MultipartFile> file) {
        Product productToUpdate = productMapper.productDTOToProduct(product);
        productToUpdate.setId(id);
        Product productSaved = productService.update(productToUpdate, file);
        return ResponseEntity.status(HttpStatus.OK).body(productSaved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a product by id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
