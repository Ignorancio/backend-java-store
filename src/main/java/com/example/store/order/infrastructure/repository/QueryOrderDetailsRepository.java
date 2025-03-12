package com.example.store.order.infrastructure.repository;

import com.example.store.order.infrastructure.entity.OrderDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryOrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Long> {

    void deleteByOrderId(Long orderId);
}
