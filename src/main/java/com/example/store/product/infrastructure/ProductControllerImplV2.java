package com.example.store.product.infrastructure;

import com.example.store.product.application.ProductServiceImplV2;
import com.example.store.product.domain.Product;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
