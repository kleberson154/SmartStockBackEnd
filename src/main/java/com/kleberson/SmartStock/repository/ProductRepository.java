package com.kleberson.SmartStock.repository;

import com.kleberson.SmartStock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByCode(String code);
    @Query("""
        SELECT p
        FROM Product p
        WHERE p.quantity <= p.minimumStock
    """)
    List<Product> findLowStockProducts();
}
