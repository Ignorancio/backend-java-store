package com.example.store.order.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderDetailsResponse(
        Long id,
        @JsonProperty("product_name")
        String productName,
        Integer quantity,
        Double price
) {
}
