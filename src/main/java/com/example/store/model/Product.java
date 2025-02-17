package com.example.store.model;

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
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "product")
    private List<OrderDetails> orderDetails;
    @OneToOne(mappedBy = "product")
    private ProductImage productImage;
}
