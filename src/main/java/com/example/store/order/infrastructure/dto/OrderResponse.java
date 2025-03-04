package com.example.store.order.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OrderResponse(
        Long id,
        String user,
        Double total,
        String status,
        @JsonProperty("order_details")
        List<OrderDetailsResponse> orderDetails
) {
}