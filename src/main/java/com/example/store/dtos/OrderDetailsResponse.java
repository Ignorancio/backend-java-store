package com.example.store.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderDetailsResponse(
        Long id,
        @JsonProperty("product_name")
        String productName,
        Integer quantity,
        Double price
) {
}
