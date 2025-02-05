package com.example.store.dtos;

public record ProductRequest(
        String name,
        String description,
        double price,
        int stock,
        String category
) {
}
