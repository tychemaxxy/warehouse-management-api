package com.shop.warehouse.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateVariantRequest(
    @NotBlank
    @Size(max = 50)
    String sku,

    @Size(max = 50)
    String size,

    @Size(max = 50)
    String color,

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 10, fraction = 2)
    BigDecimal price,

    @NotNull
    @Min(0)
    Integer stock
) {
}