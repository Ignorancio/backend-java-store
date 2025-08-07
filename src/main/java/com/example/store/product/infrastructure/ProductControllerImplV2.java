package com.example.store.product.infrastructure;

import com.example.store.product.application.ProductServiceImplV2;
import com.example.store.product.domain.Product;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/products")
@RequiredArgsConstructor
public class ProductControllerImplV2 {

    private final ProductServiceImplV2 productService;

    @GetMapping
    @Operation(summary = "Returns a paginated list of products")
    public ResponseEntity<Page<Product>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll(pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Returns a paginated list of products containing the specified name")
    public ResponseEntity<Page<Product>> findByNameContaining(@RequestParam String name, @PageableDefault Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findByNameContaining(name, pageable));
    }

    @GetMapping("/images/{fileName:.+}")
    @Operation(summary = "Return a product image by file name")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findImageByName(fileName));
    }
}
