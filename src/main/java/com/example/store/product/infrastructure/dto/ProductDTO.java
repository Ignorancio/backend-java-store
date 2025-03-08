package com.example.store.product.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductDTO(
        @NotBlank(message = "nombre del producto requerido")
        String name,
        @NotBlank(message = "descripción requerida")
        String description,
        @Positive(message = "precio debe ser mayor a 0")
        Double price,
        @PositiveOrZero(message = "stock debe ser mayor o igual a 0")
        Integer stock,
        @NotBlank(message = "categoría requerida")
        String category
) {
}
