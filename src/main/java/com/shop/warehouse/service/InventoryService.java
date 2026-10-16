package com.shop.warehouse.service;

import com.shop.warehouse.dto.SaleRequest;
import com.shop.warehouse.dto.SaleResponse;
import com.shop.warehouse.entity.ProductVariant;
import com.shop.warehouse.exception.BadRequestException;
import com.shop.warehouse.exception.ResourceNotFoundException;
import com.shop.warehouse.repository.ProductVariantRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final ProductVariantRepository variantRepository;

    public InventoryService(
        ProductVariantRepository variantRepository
    ) {
        this.variantRepository = variantRepository;
    }

    @Transactional
    public SaleResponse sell(
        Long variantId,
        SaleRequest request
    ) {
        ProductVariant variant = variantRepository
            .findByIdForUpdate(variantId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Variant not found: " + variantId
                )
            );

        int quantity = request.quantity();

        if (variant.getStock() < quantity) {
            throw new BadRequestException(
                "Insufficient stock. Available: "
                    + variant.getStock()
            );
        }

        variant.setStock(variant.getStock() - quantity);

        variantRepository.save(variant);

        return new SaleResponse(
            variant.getId(),
            variant.getSku(),
            quantity,
            variant.getStock(),
            "Sale completed successfully"
        );
    }
}