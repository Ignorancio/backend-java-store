package com.example.store.dtos;

public record ProductUpdateRequest(
        long id,
        String name,
        String description,
        double price,
        int stock,
        String category
) {
}
