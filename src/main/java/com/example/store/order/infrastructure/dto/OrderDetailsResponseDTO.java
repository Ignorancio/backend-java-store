package com.example.store.order.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderDetailsResponseDTO(
        Long id,
        @JsonProperty("product_name")
        String productName,
        Integer quantity,
        Double price
) {
}
