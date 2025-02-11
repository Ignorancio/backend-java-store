package com.example.store.service;

import com.example.store.dtos.*;
import com.example.store.model.Category;
import com.example.store.model.Product;
import com.example.store.repository.CategoryRepository;
import com.example.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category categorycheck = categoryRepository.findByName(categoryRequest.name()).orElse(null);
        if(categorycheck!=null){
            throw new IllegalArgumentException("Categoria ya existe");
        }
        Category category = Category.builder()
                .name(categoryRequest.name())
                .build();
        Category categorySave = categoryRepository.save(category);
        return new CategoryResponse(categorySave.getId(),categorySave.getName());
    }
    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        return new CategoryResponse(category.getId(), category.getName());
    }
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }
    public CategoryResponse updateCategory(CategoryUpdateRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryRequest.id()).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        category.setName(categoryRequest.name());
        categoryRepository.save(category);
        return new CategoryResponse(category.getId(), category.getName());
    }
    public CategoryResponse deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        if(category.getProducts()!=null){
            throw new IllegalArgumentException("Existen productos asociados a esta categoria");
        }
        categoryRepository.delete(category);
        return new CategoryResponse(category.getId(), category.getName());
    }
    private Category assingCategory(ProductRequest productRequest){
        Category category = categoryRepository.findByName(productRequest.category()).orElse(
                categoryRepository.save(Category.builder().name(productRequest.category()).build())
        );
        return category;
    }
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .stock(productRequest.stock())
                .build();
        Category categoryResponse = assingCategory(productRequest);
        product.setCategory(categoryResponse);
        productRepository.save(product);
        ProductResponse productResponse = new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getName());
        return productResponse;
    }
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return null;
        }
        ProductResponse productResponse = new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getName());
        return productResponse;
    }
    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getName()))
                .toList();
    }
    public ProductResponse updateProduct(ProductUpdateRequest productRequest) {
        Product product = productRepository.findById(productRequest.id()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setStock(productRequest.stock());
        Category categoryResponse = assingCategory(new ProductRequest(productRequest.name(), productRequest.description(), productRequest.price(), productRequest.stock(), productRequest.category()));
        product.setCategory(categoryResponse);
        productRepository.save(product);
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getName());
    }
    public ProductResponse deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        if(product.getOrderDetails()!=null){
            throw new IllegalArgumentException("Existen ordenes asociadas a este producto");
        }
        productRepository.delete(product);
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getName());
    }
}
