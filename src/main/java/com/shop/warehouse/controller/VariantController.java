package com.shop.warehouse.controller;

import com.shop.warehouse.dto.UpdateVariantRequest;
import com.shop.warehouse.dto.VariantResponse;
import com.shop.warehouse.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variants")
public class VariantController {

    private final ProductService productService;

    public VariantController(ProductService productService) {
        this.productService = productService;
    }

    @PutMapping("/{id}")
    public VariantResponse update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateVariantRequest request
    ) {
        return productService.updateVariant(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteVariant(id);
    }
}