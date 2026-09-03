package com.kleberson.SmartStock.service;

import com.kleberson.SmartStock.dto.product.ProductCreateRequest;
import com.kleberson.SmartStock.dto.product.ProductResponse;
import com.kleberson.SmartStock.dto.product.ProductUpdateRequest;
import com.kleberson.SmartStock.entity.Product;
import com.kleberson.SmartStock.exception.ProductAlreadyExistsException;
import com.kleberson.SmartStock.exception.ProductNotFoundException;
import com.kleberson.SmartStock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCode(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity(),
                product.getMinimumStock(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public ProductResponse create(ProductCreateRequest productCreateRequest) {
        if (productRepository.findByCode(productCreateRequest.getCode()).isPresent()) {
            throw new ProductAlreadyExistsException("Product with code " + productCreateRequest.getCode() + " already exists.");
        }

        Product product = new Product();
        product.setName(productCreateRequest.getName());
        product.setCode(productCreateRequest.getCode());
        product.setCategory(productCreateRequest.getCategory());
        product.setPrice(productCreateRequest.getPrice());
        product.setQuantity(productCreateRequest.getQuantity());
        product.setMinimumStock(productCreateRequest.getMinimumStock());

        Product savedProduct = productRepository.save(product);

        return toResponse(savedProduct);
    }

    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository
                .findAll(pageable)
                .map(this::toResponse);
    }

    public ProductResponse findById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));

        return toResponse(product);
    }

    public ProductResponse update(UUID id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));

        Product productWithSameCode = productRepository.findByCode(request.getCode())
                .filter(p -> !p.getId().equals(id))
                .orElse(null);

        if (productWithSameCode != null) {
            throw new ProductAlreadyExistsException("Product with code " + request.getCode() + " already exists.");
        }

        product.setName(request.getName());
        product.setCode(request.getCode());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setMinimumStock(request.getMinimumStock());

        Product updatedProduct = productRepository.save(product);

        return toResponse(updatedProduct);
    }

    public void delete(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));

        productRepository.delete(product);
    }

    public List<ProductResponse> findLowStockProducts() {
        return productRepository.findLowStockProducts()
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
