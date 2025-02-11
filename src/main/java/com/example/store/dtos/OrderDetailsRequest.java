package com.example.store.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderDetailsRequest(
        @JsonProperty("product_id")
        @NotNull(message = "product_id requerido")
        Long productId,
        @Positive(message=" quantity debe ser mayor a 0")
        Integer quantity,
        @Positive(message = "price debe ser mayor a 0")
        Double price
) {
}
