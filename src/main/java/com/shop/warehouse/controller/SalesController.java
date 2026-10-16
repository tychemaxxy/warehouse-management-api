package com.shop.warehouse.controller;

import com.shop.warehouse.dto.SaleRequest;
import com.shop.warehouse.dto.SaleResponse;
import com.shop.warehouse.service.InventoryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variants")
public class SalesController {

    private final InventoryService inventoryService;

    public SalesController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/{id}/sales")
    public SaleResponse sell(
        @PathVariable Long id,
        @Valid @RequestBody SaleRequest request
    ) {
        return inventoryService.sell(id, request);
    }
}