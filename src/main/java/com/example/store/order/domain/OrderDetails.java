package com.example.store.order.domain;

import com.example.store.product.domain.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetails {
    private Long id;
    private Product product;
    private Integer quantity;
    private Double price;

}
