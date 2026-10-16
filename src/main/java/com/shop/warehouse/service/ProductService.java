
package com.shop.warehouse.service;

import com.shop.warehouse.dto.*;
import com.shop.warehouse.entity.Product;
import com.shop.warehouse.entity.ProductVariant;
import com.shop.warehouse.exception.BadRequestException;
import com.shop.warehouse.exception.ResourceNotFoundException;
import com.shop.warehouse.repository.ProductRepository;
import com.shop.warehouse.repository.ProductVariantRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    public ProductService(
        ProductRepository productRepository,
        ProductVariantRepository variantRepository
    ) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
    }

    // Membuat produk baru
    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Product product = new Product(
            request.name().trim(),
            request.description()
        );

        return ProductResponse.from(
            productRepository.save(product)
        );
    }

    // Mengambil semua produk
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
            .stream()
            .map(ProductResponse::from)
            .toList();
    }

    // Mengambil produk berdasarkan ID
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return ProductResponse.from(getProduct(id));
    }

    // Memperbarui data produk
    @Transactional
    public ProductResponse update(
        Long id,
        UpdateProductRequest request
    ) {
        Product product = getProduct(id);

        product.setName(request.name().trim());
        product.setDescription(request.description());

        return ProductResponse.from(
            productRepository.save(product)
        );
    }

    // Menghapus produk
    @Transactional
    public void delete(Long id) {
        Product product = getProduct(id);
        productRepository.delete(product);
    }

    // Menambahkan variant ke produk
    @Transactional
public VariantResponse addVariant(
    Long productId,
    CreateVariantRequest request
) {
    Product product = getProduct(productId);

    String sku = request.sku().trim();

    if (variantRepository.existsBySkuIgnoreCase(sku)) {
        throw new BadRequestException(
            "SKU already exists: " + sku
        );
    }

    ProductVariant variant = new ProductVariant(
        sku,
        request.size(),
        request.color(),
        request.price(),
        request.stock()
    );

    variant.setProduct(product);

    ProductVariant savedVariant =
        variantRepository.saveAndFlush(variant);

    return VariantResponse.from(savedVariant);
}

    // Memperbarui variant
    @Transactional
    public VariantResponse updateVariant(
        Long variantId,
        UpdateVariantRequest request
    ) {
        ProductVariant variant = variantRepository
            .findById(variantId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Variant not found: " + variantId
                )
            );

        String sku = request.sku().trim();

        // Memastikan SKU tidak digunakan variant lain
        if (variantRepository.existsBySkuIgnoreCaseAndIdNot(
            sku, variantId
        )) {
            throw new BadRequestException(
                "SKU already exists: " + sku
            );
        }

        variant.setSku(sku);
        variant.setSize(request.size());
        variant.setColor(request.color());
        variant.setPrice(request.price());
        variant.setStock(request.stock());

        return VariantResponse.from(
            variantRepository.save(variant)
        );
    }

    // Menghapus variant
    @Transactional
    public void deleteVariant(Long variantId) {
        ProductVariant variant = variantRepository
            .findById(variantId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Variant not found: " + variantId
                )
            );

        variantRepository.delete(variant);
    }

    // Helper untuk mencari produk
    private Product getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Product not found: " + id
                )
            );
    }
}