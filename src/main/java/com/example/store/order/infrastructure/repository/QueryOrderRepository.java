package com.example.store.order.infrastructure.repository;

import com.example.store.order.infrastructure.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QueryOrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUser_Id(UUID userId);

}
