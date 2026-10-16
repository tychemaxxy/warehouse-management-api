package com.shop.warehouse.dto;

public record SaleResponse(
    Long variantId,
    String sku,
    int quantitySold,
    int remainingStock,
    String message
) {
}