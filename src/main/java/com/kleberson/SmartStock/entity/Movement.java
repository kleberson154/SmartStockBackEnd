package com.kleberson.SmartStock.entity;

import com.kleberson.SmartStock.enums.MovementType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movements")
public class Movement extends BaseEntity {
    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 255)
    private String observation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
