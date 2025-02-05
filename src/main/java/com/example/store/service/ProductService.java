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
        Category category = Category.builder()
                .name(categoryRequest.name())
                .build();
        Category categorySave = categoryRepository.save(category);
        return new CategoryResponse(categorySave.getId(),categorySave.getName());
    }
    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category==null){
            return null;
        }
        return new CategoryResponse(category.getId(), category.getName());
    }
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }
    public CategoryResponse updateCategory(CategoryUpdateRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryRequest.id()).orElse(null);
        if(category == null) {
            return null;
        }
        category.setName(categoryRequest.name());
        categoryRepository.save(category);
        return new CategoryResponse(category.getId(), category.getName());
    }
    public CategoryResponse deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) {
            return null;
        }
        categoryRepository.delete(category);
        return new CategoryResponse(category.getId(), category.getName());
    }
    private CategoryResponse asingCategory(ProductRequest productRequest){
        if(productRequest.category()!=null &&
                !productRequest.category().isEmpty()){
            Category category = categoryRepository.findByName(productRequest.category()).orElse(null);
            if(category == null){
                return createCategory(new CategoryRequest(productRequest.category()));
            }
            else {
                return new CategoryResponse(category.getId(), category.getName());
            }
        }
        return null;
        }
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .stock(productRequest.stock())
                .build();
        CategoryResponse categoryResponse = asingCategory(productRequest);
        if(categoryResponse!=null){
            Category category = categoryRepository.findById(categoryResponse.id()).orElse(null);
            product.setCategory(category);
        }
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
        Product product = productRepository.findById(productRequest.id()).orElse(null);
        if(product == null) {
            return null;
        }
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setStock(productRequest.stock());
        CategoryResponse categoryResponse = asingCategory(new ProductRequest(productRequest.name(), productRequest.description(), productRequest.price(), productRequest.stock(), productRequest.category()));
        if(categoryResponse!=null){
            Category category = categoryRepository.findById(categoryResponse.id()).orElse(null);
            product.setCategory(category);
        }
        productRepository.save(product);
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getName());
    }
    public ProductResponse deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return null;
        }
        productRepository.delete(product);
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getName());
    }
}
