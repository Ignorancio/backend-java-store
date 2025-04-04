package com.example.store.category.infrastructure;

import com.example.store.category.domain.Category;
import com.example.store.category.domain.CategoryService;
import com.example.store.category.infrastructure.dto.CategoryDTO;
import com.example.store.category.infrastructure.mapper.CategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Services", description = "Operations related to categories")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new category")
    public ResponseEntity<Category> save(@Valid @RequestBody CategoryDTO category) {

        Category savedCategory = categoryService.save(categoryMapper.categoryDTOToCategory(category));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @GetMapping
    @Operation(summary = "Return all categories")
    @SecurityRequirements
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Return a category by id")
    @SecurityRequirements
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a category")
    public ResponseEntity<Category> update(@PathVariable Long id,@Valid @RequestBody CategoryDTO category) {
        Category category1 = categoryMapper.categoryDTOToCategory(category);
        category1.setId(id);
        Category savedCategory = categoryService.update(category1);
        return ResponseEntity.status(HttpStatus.OK).body(savedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a category by id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
