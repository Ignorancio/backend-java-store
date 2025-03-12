package com.example.store.product.infrastructure.entity;

import com.example.store.category.infrastructure.entity.CategoryEntity;
import com.example.store.order.infrastructure.entity.OrderDetailsEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "orderDetails")
@ToString(exclude = "orderDetails")
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
    @OneToMany(mappedBy = "product")
    private List<OrderDetailsEntity> orderDetails;
    @OneToOne(mappedBy = "product", cascade = CascadeType.PERSIST)
    private ProductImageEntity productImage;
}
