package com.example.store.order.infrastructure;

import com.example.store.order.domain.Order;
import com.example.store.order.infrastructure.dto.OrderDTO;
import com.example.store.order.infrastructure.dto.OrderResponseAdminDTO;
import com.example.store.order.infrastructure.dto.OrderResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderController {

    ResponseEntity<OrderResponseDTO> save(OrderDTO order);

    ResponseEntity<Order> findById(Long id);

    ResponseEntity<List<OrderResponseAdminDTO>> findAll();

    ResponseEntity<List<OrderResponseDTO>> findByUserId();

    ResponseEntity<Void> delete(Long id);
}
