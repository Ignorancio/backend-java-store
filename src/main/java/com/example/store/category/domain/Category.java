package com.example.store.category.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Category {
    private Long id;
    @NotBlank(message = "nombre de la categoría requerido")
    private String name;
}
