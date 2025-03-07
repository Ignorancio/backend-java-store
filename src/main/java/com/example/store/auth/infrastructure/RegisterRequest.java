package com.example.store.auth.infrastructure;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "email requerido")
        @Email(message = "email inválido")
        String email,
        @NotBlank(message = "password requerido")
        String password
) {
}
