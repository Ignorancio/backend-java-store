package com.example.store.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderDetailsRequest(
        @JsonProperty("product_id")
        Long productId,
        Integer quantity,
        Double price
) {
}
