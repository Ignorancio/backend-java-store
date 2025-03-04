package com.example.store.product.application;

import com.example.store.product.infrastructure.entity.ProductEntity;
import com.example.store.product.infrastructure.entity.ProductImageEntity;
import com.example.store.product.infrastructure.repository.implementation.ProductImageRepository;
import com.example.store.product.infrastructure.repository.implementation.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ProductImageServiceImpl {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    public void saveImage(String productName,MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString();
            if(file.getOriginalFilename() == null){
                throw new IllegalArgumentException("Imagen no encontrada");
            }
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            byte[] bytes = file.getBytes();
            long fileSize = file.getSize();
            long MaxSize = 5 * 1024 * 1024;

            if (fileSize > MaxSize) {
                throw new IllegalArgumentException("El tamaño de la imagen es muy grande");
            }

            if (!extension.equals(".png") && !extension.equals(".jpg") && !extension.equals(".jpeg")) {
                throw new IllegalArgumentException("Formato de imagen no soportado");
            }

            File folder = new File("src/main/resources/images");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            Path path = Paths.get("src/main/resources/images/" + fileName + extension);
            Files.write(path, bytes);
            ProductEntity product = productRepository.findByName(productName).orElseThrow(()-> new IllegalArgumentException("Producto no encontrado"));
            ProductImageEntity productImage = ProductImageEntity.builder()
                    .url("api/v1/products/images/" + fileName + extension)
                    .product(product)
                    .build();
            productImageRepository.save(productImage);
        }catch (Exception e){
            throw new IllegalArgumentException("Error al guardar la imagen");
        }
    }

    public void deleteImage(Long id) {
        ProductImageEntity productImage = productImageRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Imagen no encontrada"));
        File file = new File("src/main/resources/images/" + productImage.getUrl().substring(productImage.getUrl().lastIndexOf("/")));
        if(file.exists()){
            file.delete();
        }
        productImageRepository.delete(productImage);
    }

    public void updateImage(String productName, MultipartFile file) {
        deleteImage(productRepository.findByName(productName).orElseThrow(()-> new IllegalArgumentException("Producto no encontrado")).getProductImage().getId());
        saveImage(productName, file);
    }
}
