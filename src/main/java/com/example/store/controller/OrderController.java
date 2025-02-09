package com.example.store.controller;

import com.example.store.dtos.OrderRequest;
import com.example.store.dtos.OrderResponse;
import com.example.store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/")
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }
    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }
    @GetMapping("/")
    public List<OrderResponse> getOrders() {
        return orderService.getUserOrders();
    }
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
