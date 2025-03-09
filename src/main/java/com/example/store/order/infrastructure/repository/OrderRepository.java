package com.example.store.order.infrastructure.repository;

import com.example.store.order.infrastructure.entity.OrderEntity;
import com.example.store.user.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUser(UserEntity user);
}
