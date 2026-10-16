package com.shop.warehouse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SaleRequest(
    @NotNull
    @Min(1)
    Integer quantity
) {
}