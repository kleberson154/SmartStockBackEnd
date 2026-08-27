package com.kleberson.SmartStock.service;

import com.kleberson.SmartStock.dto.product.ProductCreateRequest;
import com.kleberson.SmartStock.dto.product.ProductResponse;
import com.kleberson.SmartStock.dto.product.ProductUpdateRequest;
import com.kleberson.SmartStock.entity.Product;
import com.kleberson.SmartStock.exception.ProductAlreadyExistsException;
import com.kleberson.SmartStock.exception.ProductNotFoundException;
import com.kleberson.SmartStock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse create(ProductCreateRequest productCreateRequest) {
            if(productRepository.findByCode(productCreateRequest.getCode()).isPresent()){
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

            return new ProductResponse(
                    savedProduct.getId(),
                    savedProduct.getName(),
                    savedProduct.getCode(),
                    savedProduct.getCategory(),
                    savedProduct.getPrice(),
                    savedProduct.getQuantity(),
                    savedProduct.getMinimumStock(),
                    savedProduct.getCreatedAt(),
                    savedProduct.getUpdatedAt()
            );
    }

    public List<ProductResponse> findAll(){
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getCode(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getMinimumStock(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                ))
                .toList();
    }

    public ProductResponse findById(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));

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

    public ProductResponse update(UUID id, ProductUpdateRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));

        product.setName(request.getName());
        product.setCode(request.getCode());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setMinimumStock(request.getMinimumStock());

        Product updatedProduct = productRepository.save(product);

        return new ProductResponse(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getCode(),
                updatedProduct.getCategory(),
                updatedProduct.getPrice(),
                updatedProduct.getQuantity(),
                updatedProduct.getMinimumStock(),
                updatedProduct.getCreatedAt(),
                updatedProduct.getUpdatedAt()
        );
    }

    public void delete(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));

        productRepository.delete(product);
    }

    public List<ProductResponse> findLowStockProducts() {
        return productRepository.findLowStockProducts()
                .stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getCode(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getMinimumStock(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                ))
                .toList();
    }
}
