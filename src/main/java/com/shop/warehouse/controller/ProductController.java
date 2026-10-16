package com.shop.warehouse.controller;

import com.shop.warehouse.dto.CreateProductRequest;
import com.shop.warehouse.dto.UpdateProductRequest;
import com.shop.warehouse.dto.CreateVariantRequest;
import com.shop.warehouse.dto.UpdateVariantRequest;
import com.shop.warehouse.dto.ProductResponse;
import com.shop.warehouse.dto.VariantResponse;
import com.shop.warehouse.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(
        @Valid @RequestBody CreateProductRequest request
    ) {
        return productService.create(request);
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateProductRequest request
    ) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PostMapping("/{productId}/variants")
    @ResponseStatus(HttpStatus.CREATED)
    public VariantResponse addVariant(
        @PathVariable Long productId,
        @Valid @RequestBody CreateVariantRequest request
    ) {
        return productService.addVariant(productId, request);
    }
}