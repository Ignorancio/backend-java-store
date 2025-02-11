package com.example.store.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "nombre requerido")
        String name
) {
}
