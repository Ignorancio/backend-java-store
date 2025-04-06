package com.example.store.order.domain;

import com.example.store.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private User user;
    private Double total;
    private String status;
    private List<OrderDetails> orderDetails;
}
