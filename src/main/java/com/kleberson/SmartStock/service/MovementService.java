package com.kleberson.SmartStock.service;

import com.kleberson.SmartStock.dto.movement.MovementCreateRequest;
import com.kleberson.SmartStock.dto.movement.MovementResponse;
import com.kleberson.SmartStock.entity.Movement;
import com.kleberson.SmartStock.entity.Product;
import com.kleberson.SmartStock.enums.MovementType;
import com.kleberson.SmartStock.exception.InsufficientStockException;
import com.kleberson.SmartStock.exception.ProductNotFoundException;
import com.kleberson.SmartStock.repository.MovementRepository;
import com.kleberson.SmartStock.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementService {
    private final MovementRepository movementRepository;
    private final ProductRepository productRepository;

    @Transactional
    public MovementResponse create(MovementCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with ID " + request.getProductId() + " not found."
                ));

        if(request.getType() == MovementType.ENTRY){
            product.setQuantity(product.getQuantity() + request.getQuantity());
        } else if (request.getType() == MovementType.EXIT){
            if (product.getQuantity() < request.getQuantity()){
                throw new InsufficientStockException(
                        "Insufficient stock for product " + product.getName()
                );
            }

            product.setQuantity(product.getQuantity() - request.getQuantity());
        }

        Movement movement = new Movement();

        movement.setType(request.getType());
        movement.setQuantity(request.getQuantity());
        movement.setObservation(request.getObservation());
        movement.setProduct(product);

        productRepository.save(product);
        Movement savedMovement = movementRepository.save(movement);

        return new MovementResponse(
                savedMovement.getId(),
                savedMovement.getType(),
                savedMovement.getQuantity(),
                savedMovement.getObservation(),
                product.getId(),
                product.getName(),
                savedMovement.getCreatedAt()
        );
    }

    public List<MovementResponse> findAll() {
        return movementRepository.findAll()
                .stream()
                .map(movement -> new MovementResponse(
                movement.getId(),
                movement.getType(),
                movement.getQuantity(),
                movement.getObservation(),
                movement.getProduct().getId(),
                movement.getProduct().getName(),
                movement.getCreatedAt()
        )).toList();
    }

    public List<MovementResponse> findByProductId(UUID productId) {
        productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(
                "Product with ID " + productId + " not found."
        ));

        return movementRepository.findByProductId(productId)
                .stream()
                .map(movement -> new MovementResponse(
                        movement.getId(),
                        movement.getType(),
                        movement.getQuantity(),
                        movement.getObservation(),
                        movement.getProduct().getId(),
                        movement.getProduct().getName(),
                        movement.getCreatedAt()
                )).toList();
    }
}
