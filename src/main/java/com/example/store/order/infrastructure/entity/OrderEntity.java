package com.example.store.order.infrastructure.entity;

import com.example.store.user.infrastructure.entity.UserEntity;
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
public class OrderEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetails= new ArrayList<>();
}
