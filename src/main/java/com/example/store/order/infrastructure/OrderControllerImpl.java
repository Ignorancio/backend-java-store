package com.example.store.order.infrastructure;

import com.example.store.order.application.OrderServiceImpl;
import com.example.store.order.infrastructure.dto.OrderRequest;
import com.example.store.order.infrastructure.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderControllerImpl {

    private final OrderServiceImpl orderService;

    @PostMapping("/")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrderByUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderResponse>> findAllOrderByUser() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getUserOrders());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> findAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(id));
    }
}
