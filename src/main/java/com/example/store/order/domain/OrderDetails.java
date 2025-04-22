package com.example.store.order.domain;

import com.example.store.product.domain.Product;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    private Long id;
    private Integer quantity;
    private Double price;
    private Product product;
}
