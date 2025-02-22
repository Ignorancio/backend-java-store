package com.example.store.controller;

import com.example.store.dtos.OrderRequest;
import com.example.store.dtos.OrderResponse;
import com.example.store.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/")
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/{id}")
    public OrderResponse findOrderByUser(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/")
    public List<OrderResponse> findAllOrderByUser() {
        return orderService.getUserOrders();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> findAllOrders() {
        return orderService.getAllOrders();
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
