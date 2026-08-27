package com.kleberson.SmartStock.controller;

import com.kleberson.SmartStock.dto.product.ProductCreateRequest;
import com.kleberson.SmartStock.dto.product.ProductResponse;
import com.kleberson.SmartStock.dto.product.ProductUpdateRequest;
import com.kleberson.SmartStock.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody ProductCreateRequest request) {

        ProductResponse product = productService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);

    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id, @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> findLowStockProducts() {
        return ResponseEntity.ok(productService.findLowStockProducts());
    }
}
