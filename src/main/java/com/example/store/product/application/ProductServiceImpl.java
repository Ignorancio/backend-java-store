package com.example.store.product.application;

import com.example.store.category.domain.Category;
import com.example.store.category.domain.CategoryRepository;
import com.example.store.product.domain.*;
import com.example.store.search.infrastructure.repository.implementation.SearchProductRepository;
import com.example.store.util.FileUpload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository queryProductRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final SearchProductRepository searchProductRepository;
    private final FileUpload fileUpload;
    private final ProductUtils productUtils;

    public ProductServiceImpl(@Qualifier("postgresProductRepository") ProductRepository queryProductRepository,
                              ProductImageRepository productImageRepository,
                              CategoryRepository categoryRepository,
                              SearchProductRepository searchProductRepository,
                              FileUpload fileUpload,
                              ProductUtils productUtils) {
        this.queryProductRepository = queryProductRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.searchProductRepository = searchProductRepository;
        this.fileUpload = fileUpload;
        this.productUtils = productUtils;
    }

    public Product save(Product product, MultipartFile file) {
        product.setCategory(findOrSaveCategory(product.getCategory()));
        String fileName = fileUpload.uploadFile("/images", file);
        ProductImage productImage = ProductImage.builder().url("api/v1/products/images/"+fileName).build();
        product.setProductImage(productImage);
        Product saved = queryProductRepository.save(product);
        searchProductRepository.save(saved);
        return saved;
    }

    public List<Product> findAll() {
        return queryProductRepository.findAll();
    }

    public Product findById(Long id) {
        return queryProductRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    public Product update(Product product, Optional<MultipartFile> file) {
        Product productdb = queryProductRepository.findById(product.getId()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        productUtils.copyNonNullProperties(product, productdb);
        productdb.setCategory(findOrSaveCategory(productdb.getCategory()));
        if(file.isPresent() && file.get().getOriginalFilename() != null) {
            String fileName = fileUpload.uploadFile("/images", file.get());
            productdb.getProductImage().setUrl("api/v1/products/images/"+fileName);
            productImageRepository.save(productdb.getProductImage());
        }
        return queryProductRepository.save(productdb);
    }

    public void deleteById(Long id) {
        queryProductRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        queryProductRepository.deleteById(id);
    }

    private Category findOrSaveCategory(Category category) {
        return categoryRepository.findByName(category.getName())
                .orElseGet(() -> categoryRepository.save(category));
    }
}
