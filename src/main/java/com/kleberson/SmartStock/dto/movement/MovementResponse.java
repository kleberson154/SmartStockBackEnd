package com.kleberson.SmartStock.dto.movement;

import com.kleberson.SmartStock.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementResponse {
    private UUID id;
    private MovementType type;
    private Integer quantity;
    private String observation;

    private UUID productId;
    private String productName;

    private LocalDateTime createdAt;
}
