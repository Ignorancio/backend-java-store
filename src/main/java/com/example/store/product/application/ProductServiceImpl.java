package com.example.store.product.application;

import com.example.store.category.domain.Category;
import com.example.store.category.domain.CategoryRepository;
import com.example.store.product.domain.*;
import com.example.store.util.FileUploadUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository queryProductRepository;
    private final ProductRepository cacheProductRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(@Qualifier("postgresProductRepository") ProductRepository queryProductRepository,
                              @Qualifier("redisProductRepository") ProductRepository cacheProductRepository,
                              CategoryRepository categoryRepository) {
        this.queryProductRepository = queryProductRepository;
        this.cacheProductRepository = cacheProductRepository;
        this.categoryRepository = categoryRepository;
    }


    public Product save(Product product, MultipartFile file) {
        product.setCategory(findOrSaveCategory(product.getCategory()));
        String fileName = FileUploadUtil.uploadFile("/images", file);
        ProductImage productImage = ProductImage.builder().url("api/v1/products/images/"+fileName).build();
        product.setProductImage(productImage);
        cacheProductRepository.save(product);
        return queryProductRepository.save(product);
    }

    public List<Product> findAll() {
        List<Product> products = cacheProductRepository.findAll();
        if(!products.isEmpty()){
            return products;
        }
        products = queryProductRepository.findAll();
        if(!products.isEmpty()){
            products.forEach(cacheProductRepository::save);
        }
        return products;
    }

    public Product findById(Long id) {
        Optional<Product> optionalProduct = cacheProductRepository.findById(id);

        if(optionalProduct.isPresent()){
            return optionalProduct.get();
        }

        Product product = queryProductRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        cacheProductRepository.save(product);
        return product;
    }

    public Product update(Product product, Optional<MultipartFile> file) {
        Product productdb = queryProductRepository.findById(product.getId()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        BeanUtils.copyProperties(product, productdb, "productImage");
        product.setCategory(findOrSaveCategory(productdb.getCategory()));
        if(file.isPresent() && file.get().getOriginalFilename() != null) {
            String fileName = FileUploadUtil.uploadFile("/images", file.get());
            ProductImage productImage = ProductImage.builder().url("api/v1/products/images/"+fileName).build();
            productdb.setProductImage(productImage);
        }
        queryProductRepository.save(productdb);
        return queryProductRepository.save(productdb);
    }

    public void deleteById(Long id) {
        queryProductRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        queryProductRepository.deleteById(id);
        cacheProductRepository.deleteById(id);
    }

    private Category findOrSaveCategory(Category category) {
        return categoryRepository.findByName(category.getName())
                .orElseGet(() -> categoryRepository.save(category));
    }
}
