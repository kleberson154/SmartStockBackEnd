package com.kleberson.SmartStock.repository;

import com.kleberson.SmartStock.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MovementRepository extends JpaRepository<Movement, UUID> {
    List<Movement> findByProductId(UUID productId);
}
