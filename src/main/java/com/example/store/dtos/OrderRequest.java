package com.example.store.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @JsonProperty("order_details")
        @NotNull(message = "No se agregaron productos a la orden")
        @Valid
        List<OrderDetailsRequest> orderDetails
) {
}
