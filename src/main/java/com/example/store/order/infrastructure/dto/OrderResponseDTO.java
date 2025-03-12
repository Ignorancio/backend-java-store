package com.example.store.order.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OrderResponseDTO(
        Long id,
        Double total,
        String status,
        @JsonProperty("order_details")
        List<OrderDetailsResponseDTO> orderDetails
) {
}