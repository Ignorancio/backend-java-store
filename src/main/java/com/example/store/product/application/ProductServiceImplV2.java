package com.example.store.product.application;

import com.example.store.product.domain.Product;
import com.example.store.product.infrastructure.repository.implementation.ProductRepositoryImplV2;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ProductServiceImplV2 {

    private final ProductRepositoryImplV2 productRepositoryImplV2;

    public Page<Product> findAll(Pageable pageable) {
        return productRepositoryImplV2.findAll(pageable);
    }

    public Page<Product> findByNameContaining(String name, Pageable pageable) {
        return productRepositoryImplV2.findAllByProductNameContaining(name, pageable);
    }

    public Resource findImageByName(String name) {
        Path imagePath = Paths.get("public/images", name);
        if (!imagePath.toFile().exists()) {
            throw new IllegalArgumentException("Image not found: " + name);
        }
        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Error retrieving image: " + name, e);
        }
    }
}
