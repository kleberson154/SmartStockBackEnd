package com.kleberson.SmartStock.service;

import com.kleberson.SmartStock.dto.product.ProductCreateRequest;
import com.kleberson.SmartStock.entity.Product;
import com.kleberson.SmartStock.exception.ProductAlreadyExistsException;
import com.kleberson.SmartStock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product create(ProductCreateRequest productCreateRequest) {
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
            return productRepository.save(product);
    }
}
