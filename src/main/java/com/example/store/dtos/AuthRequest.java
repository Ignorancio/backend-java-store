package com.example.store.dtos;

public record AuthRequest(
        String email,
        String password
) {
}
