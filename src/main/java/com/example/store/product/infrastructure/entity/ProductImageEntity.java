package com.example.store.product.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity(name = "product_images")
public class ProductImageEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String url;
}
