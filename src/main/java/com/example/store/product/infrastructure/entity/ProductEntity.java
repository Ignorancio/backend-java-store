package com.example.store.product.infrastructure.entity;

import com.example.store.category.infrastructure.entity.CategoryEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer stock;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval = true)
    @JoinColumn(name = "product_image_id")
    private ProductImageEntity productImage;
}
