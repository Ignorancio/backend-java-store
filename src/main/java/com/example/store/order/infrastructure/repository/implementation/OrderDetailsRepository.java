package com.example.store.order.infrastructure.repository.implementation;

import com.example.store.order.infrastructure.entity.OrderDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Long> {

}
