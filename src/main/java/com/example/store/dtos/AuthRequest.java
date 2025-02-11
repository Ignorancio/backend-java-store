package com.example.store.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "email requerido")
        @Email(message = "email inválido")
        String email,
        @NotBlank(message = "password requerido")
        String password
) {
}
