package com.shop.warehouse.dto;

import com.shop.warehouse.entity.Product;
import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse(
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    List<VariantResponse> variants
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getCreatedAt(),
            product.getVariants()
                .stream()
                .map(VariantResponse::from)
                .toList()
        );
    }
}