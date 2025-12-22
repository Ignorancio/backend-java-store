package com.example.store.order.infrastructure;

import com.example.store.order.domain.Order;
import com.example.store.order.infrastructure.dto.OrderDTO;
import com.example.store.user.infrastructure.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderController {

    ResponseEntity<Order> save(UserEntity user, OrderDTO order);

    ResponseEntity<Order> findById(UserEntity user, Long id);

    ResponseEntity<List<Order>> findAll();

    ResponseEntity<List<Order>> findByUserId(UserEntity user);

    ResponseEntity<Void> delete(UserEntity user, Long id);

    ResponseEntity<Order> update(UserEntity user, Long id, OrderDTO orderDTO);
}
