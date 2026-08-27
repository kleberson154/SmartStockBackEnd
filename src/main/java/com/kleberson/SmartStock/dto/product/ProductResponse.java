package com.kleberson.SmartStock.dto.product;

import com.kleberson.SmartStock.entity.Product;
import com.kleberson.SmartStock.exception.ProductAlreadyExistsException;
import com.kleberson.SmartStock.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String name;
    private String code;
    private String category;
    private BigDecimal price;
    private Integer quantity;
    private Integer minimumStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
