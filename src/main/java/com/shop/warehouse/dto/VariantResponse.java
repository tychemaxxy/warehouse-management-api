package com.shop.warehouse.dto;

import com.shop.warehouse.entity.ProductVariant;
import java.math.BigDecimal;

public record VariantResponse(
    Long id,
    String sku,
    String size,
    String color,
    BigDecimal price,
    int stock
) {
    public static VariantResponse from(ProductVariant variant) {
        return new VariantResponse(
            variant.getId(),
            variant.getSku(),
            variant.getSize(),
            variant.getColor(),
            variant.getPrice(),
            variant.getStock()
        );
    }
}