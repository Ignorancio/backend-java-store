package com.example.store.search.infrastructure.entity;

import com.example.store.category.domain.Category;
import com.example.store.product.domain.ProductImage;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "product")
@Data
public class ProductElasticEntity {
    @Id
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Category category;
    private ProductImage productImage;
}
