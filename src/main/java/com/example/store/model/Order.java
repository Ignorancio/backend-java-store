package com.example.store.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double total;

    private String status;

    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetails= new ArrayList<>();
}
