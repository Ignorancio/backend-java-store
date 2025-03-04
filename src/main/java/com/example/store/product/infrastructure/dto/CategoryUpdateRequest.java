package com.example.store.product.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryUpdateRequest(
        @NotNull(message = "id requerido")
        Long id,
        @NotBlank(message = "nombre requerido")
        String name
) {
}
