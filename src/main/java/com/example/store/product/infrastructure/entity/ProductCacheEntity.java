package com.example.store.product.infrastructure.entity;

import com.example.store.category.domain.Category;
import com.example.store.product.domain.ProductImage;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("product")
@Data
public class ProductCacheEntity implements Serializable {
    @Id
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Category category;
    private ProductImage productImage;
}
