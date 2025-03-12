package com.example.store.order.domain;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order save(Order order);

    Order findById(Long id);

    List<Order> findAll();

    List<Order> findByUserId(UUID userId);

    void delete(Long id);
}
