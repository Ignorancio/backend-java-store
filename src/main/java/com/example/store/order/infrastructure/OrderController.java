package com.example.store.order.infrastructure;

import com.example.store.order.domain.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderController {

    ResponseEntity<Order> save(Order order);

    ResponseEntity<Order> findById(Long id);

    ResponseEntity<List<Order>> findAll();

    ResponseEntity<List<Order>> findByUserId(Authentication authentication);

    ResponseEntity<Void> delete(Long id);
}
