package com.kleberson.SmartStock.dto.movement;

import com.kleberson.SmartStock.enums.MovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementCreateRequest {
    @NotNull(message = "Product ID is required")
    private UUID productId;
    @NotNull(message = "Movement type is required")
    private MovementType type;
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be a positive value")
    private Integer quantity;
    @Size(max = 255, message = "Observation must not exceed 255 characters")
    private String observation;
}
