package com.example.store.dtos;

public record ProductResponse(
        Long id,
        String name,
        String description,
        double price,
        int stock,
        String category
) {
}
