package com.example.store.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "product")
@ToString(exclude = "product")
@Entity(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String url;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
