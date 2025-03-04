package com.example.store.product.application;

import com.example.store.product.infrastructure.dto.*;
import com.example.store.product.infrastructure.entity.CategoryEntity;
import com.example.store.product.infrastructure.entity.ProductEntity;
import com.example.store.product.infrastructure.repository.implementation.CategoryRepository;
import com.example.store.product.infrastructure.repository.implementation.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageServiceImpl productImageService;

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if(categoryRepository.existsByName(categoryRequest.name())){
            throw new IllegalArgumentException("Categoria ya existe");
        }
        CategoryEntity category = CategoryEntity.builder()
                .name(categoryRequest.name())
                .build();
        CategoryEntity categorySave = categoryRepository.save(category);
        return new CategoryResponse(categorySave.getId(),categorySave.getName());
    }

    public CategoryResponse getCategory(Long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        return new CategoryResponse(category.getId(), category.getName());
    }

    public List<CategoryResponse> getCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }

    public CategoryResponse updateCategory(CategoryUpdateRequest categoryRequest) {
        CategoryEntity category = categoryRepository.findById(categoryRequest.id()).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        category.setName(categoryRequest.name());
        categoryRepository.save(category);
        return new CategoryResponse(category.getId(), category.getName());
    }

    public CategoryResponse deleteCategory(Long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        if(category.getProducts()!=null){
            throw new IllegalArgumentException("Existen productos asociados a esta categoria");
        }
        categoryRepository.delete(category);
        return new CategoryResponse(category.getId(), category.getName());
    }

    private CategoryEntity assingCategory(ProductRequest productRequest){
        return categoryRepository.findByName(productRequest.category()).orElseGet(() -> categoryRepository.save(
                CategoryEntity.builder().name(productRequest.category()).build()
        ));
    }

    public ProductResponse createProduct(ProductRequest productRequest, MultipartFile file) {
        ProductEntity product = ProductEntity.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .stock(productRequest.stock())
                .build();
        CategoryEntity categoryResponse = assingCategory(productRequest);
        product.setCategory(categoryResponse);
        productRepository.save(product);
        if(file != null && !file.isEmpty()) {
            productImageService.saveImage(product.getName(), file);
        }
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getName());
    }

    public ProductResponse getProduct(Long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(), product.getStock(),
                product.getCategory().getName());
    }

    public List<ProductResponse> getProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getName()))
                .toList();
    }

    public ProductResponse updateProduct(ProductUpdateRequest productRequest, MultipartFile file) {
        ProductEntity product = productRepository.findById(productRequest.id()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setStock(productRequest.stock());
        CategoryEntity categoryResponse = assingCategory(new ProductRequest(productRequest.name(), productRequest.description(), productRequest.price(), productRequest.stock(), productRequest.category()));
        product.setCategory(categoryResponse);
        productRepository.save(product);
        if(file != null && !file.isEmpty()) {
            productImageService.updateImage(product.getName(), file);
        }
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getName());
    }

    public ProductResponse deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        if(!product.getOrderDetails().isEmpty()){
            throw new IllegalArgumentException("Existen ordenes asociadas a este producto");
        }
        if(product.getProductImage()!=null){
            productImageService.deleteImage(product.getProductImage().getId());
        }
        productRepository.delete(product);
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getName());
    }
}
