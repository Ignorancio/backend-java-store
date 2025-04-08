package com.example.store.order.domain;

import com.example.store.product.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@ToString//(exclude = "order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
//    @JsonIgnore
//    private Order order;
    private Long id;
    private Integer quantity;
    private Double price;
    private Product product;
}
