package com.example.store.product.infrastructure;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "nombre requerido")
        String name
) {
}
