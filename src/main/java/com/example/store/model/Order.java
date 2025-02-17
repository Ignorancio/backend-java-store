package com.example.store.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"user","orderDetails"})
@ToString(exclude = {"user","orderDetails"})
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
