package com.example.store.dtos;

public record RegisterRequest(
        String email,
        String password
) {
}
