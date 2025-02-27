package com.example.store.order.infrastructure.entity;

import com.example.store.product.infrastructure.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"order", "product"})
@ToString(exclude = {"order", "product"})
@Entity(name = "order_details")
public class OrderDetails {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Double price;

}
