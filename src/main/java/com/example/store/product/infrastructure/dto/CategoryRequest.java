package com.example.store.product.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "nombre requerido")
        String name
) {
}
