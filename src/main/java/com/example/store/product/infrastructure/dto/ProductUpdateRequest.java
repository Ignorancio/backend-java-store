package com.example.store.product.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateRequest(
        @NotNull(message = "id requerido")
        Long id,
        @NotBlank(message = "nombre requerido")
        String name,
        @NotBlank(message = "descripción requerida")
        String description,
        @Positive(message = "precio debe ser mayor a 0")
        double price,
        @PositiveOrZero(message = "stock debe ser mayor o igual a 0")
        int stock,
        @NotBlank(message = "categoría requerida")
        String category
) {
}
