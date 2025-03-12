package com.example.store.order.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderDetailsDTO(
        @JsonProperty("product_id")
        @NotNull(message = "product_id requerido")
        Long productId,
        @Positive(message=" quantity debe ser mayor a 0")
        Integer quantity
) {
}
