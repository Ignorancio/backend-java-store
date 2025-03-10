package com.example.store.product.application;

import com.example.store.category.domain.CategoryRepository;
import com.example.store.product.domain.*;
import com.example.store.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product save(Product product, MultipartFile file) {
        if(!categoryRepository.existsByName(product.getCategory().getName())){
            product.setCategory(categoryRepository.save(product.getCategory()));
        }
        else {
            product.setCategory(categoryRepository.findByName(product.getCategory().getName()).get());
        }
        String fileName = FileUploadUtil.uploadFile("/images", file);
        ProductImage productImage = ProductImage.builder().url("api/v1/products/images/"+fileName).build();
        product.setProductImage(productImage);
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    public Product update(Product product, Optional<MultipartFile> file) {
        Product productdb = productRepository.findById(product.getId()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        BeanUtils.copyProperties(product, productdb, "productImage");
        if(!categoryRepository.existsByName(product.getCategory().getName())){
            product.setCategory(categoryRepository.save(product.getCategory()));
        }
        else {
            product.setCategory(categoryRepository.findByName(product.getCategory().getName()).get());
        }
        if(file.isPresent() && file.get().getOriginalFilename() != null) {
            String fileName = FileUploadUtil.uploadFile("/images", file.get());
            ProductImage productImage = ProductImage.builder().url("api/v1/products/images/"+fileName).build();
            productdb.setProductImage(productImage);
        }
        return productRepository.save(productdb);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
