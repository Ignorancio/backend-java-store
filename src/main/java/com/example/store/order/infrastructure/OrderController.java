package com.example.store.order.infrastructure;

import com.example.store.order.domain.Order;
import com.example.store.order.infrastructure.dto.OrderDTO;
import com.example.store.order.infrastructure.dto.OrderDetailsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderController {

    ResponseEntity<Order> save(OrderDTO order);

    ResponseEntity<Order> findById(Long id);

    ResponseEntity<List<Order>> findAll();

    ResponseEntity<List<Order>> findByUserId();

    ResponseEntity<Void> delete(Long id);

    ResponseEntity<Order> update(Long id, List<OrderDetailsDTO> orderDTO);
}
