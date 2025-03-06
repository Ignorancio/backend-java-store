package com.example.store.category.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
        @NotBlank(message = "nombre requerido")
        String name
) {
}