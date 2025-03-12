package com.example.store.order.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OrderResponseAdminDTO(
        Long id,
        @JsonProperty("user_email")
        String userEmail,
        Double total,
        String status,
        @JsonProperty("order_details")
        List<OrderDetailsResponseDTO> orderDetails
) {
}
