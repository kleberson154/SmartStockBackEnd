package com.kleberson.SmartStock.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {
    private String name;
    private String code;
    private String category;
    private BigDecimal price;
    private Integer quantity;
    private Integer minimumStock;
}
