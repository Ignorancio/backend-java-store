package com.example.store.product.infrastructure.dto;

public record ProductResponse(
        Long id,
        String name,
        String description,
        double price,
        int stock,
        String category
) {
}
