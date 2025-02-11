package com.example.store.controller;

import com.example.store.dtos.CategoryRequest;
import com.example.store.dtos.CategoryResponse;
import com.example.store.dtos.CategoryUpdateRequest;
import com.example.store.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse category = productService.getCategory(id);
        if(category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }
    @GetMapping("/")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }
    @PostMapping("/")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest category) {
        CategoryResponse categorySaved = productService.createCategory(category);
        return ResponseEntity.ok(categorySaved);
    }
    @PutMapping("/")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryUpdateRequest category) {
        CategoryResponse categorySaved = productService.updateCategory(category);
        if(categorySaved == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categorySaved);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id) {
        CategoryResponse category = productService.deleteCategory(id);
        if(category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

}
