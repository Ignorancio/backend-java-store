package com.example.store.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OrderRequest(
        @JsonProperty("order_details")
        List<OrderDetailsRequest> orderDetails
) {
}
