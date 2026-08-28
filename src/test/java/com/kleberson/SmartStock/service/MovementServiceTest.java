package com.kleberson.SmartStock.service;

import com.kleberson.SmartStock.dto.movement.MovementCreateRequest;
import com.kleberson.SmartStock.entity.Movement;
import com.kleberson.SmartStock.entity.Product;
import com.kleberson.SmartStock.enums.MovementType;
import com.kleberson.SmartStock.exception.InsufficientStockException;
import com.kleberson.SmartStock.exception.ProductNotFoundException;
import com.kleberson.SmartStock.repository.MovementRepository;
import com.kleberson.SmartStock.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovementServiceTest {
    @Mock
    private MovementRepository movementRepository;
    @Mock
    private ProductRepository productRepository;

    private MovementService movementService;
    private Product product;

    @BeforeEach
    void setUp() {
        movementService = new MovementService(movementRepository, productRepository);
        product = new Product();
        product.setName("Teclado");
        product.setCode("TEC-001");
        product.setCategory("Periferico");
        product.setPrice(new java.math.BigDecimal("199.90"));
        product.setQuantity(10);
        product.setMinimumStock(3);
    }

    @Test
    void testEntryIncreasesStock() {
        UUID productId = UUID.randomUUID();
        product.setId(productId);

        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));

        when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MovementCreateRequest request = new MovementCreateRequest();
        request.setProductId(product.getId());
        request.setQuantity(5);
        request.setType(MovementType.ENTRY);

        movementService.create(request);

        assertEquals(15, product.getQuantity());

        verify(movementRepository).save(any(Movement.class));
        verify(productRepository).save(product);
    }

    @Test
    void testExitDecreasesStock(){
        UUID productId = UUID.randomUUID();
        product.setId(productId);

        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));

        when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MovementCreateRequest request = new MovementCreateRequest();
        request.setProductId(product.getId());
        request.setQuantity(4);
        request.setType(MovementType.EXIT);

        movementService.create(request);

        assertEquals(6, product.getQuantity());

        verify(movementRepository).save(any(Movement.class));
        verify(productRepository).save(product);
    }

    @Test
    void testExitWithInsufficientStockThrowsException(){
       UUID productId = UUID.randomUUID();
       product.setId(productId);

       when (productRepository.findById(product.getId()))
               .thenReturn(Optional.of(product));

       MovementCreateRequest request = new MovementCreateRequest();
       request.setProductId(product.getId());
       request.setQuantity(15);
       request.setType(MovementType.EXIT);

       assertThrows(InsufficientStockException.class, () -> movementService.create(request));

       assertEquals(10, product.getQuantity());

       verify(movementRepository, never()).save(any(Movement.class));
       verify(productRepository, never()).save(product);
    }

    @Test
    void testCreateMovementWithProductNotFoundThrowsException(){
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        MovementCreateRequest request = new MovementCreateRequest();
        request.setProductId(productId);
        request.setQuantity(5);
        request.setType(MovementType.ENTRY);

        assertThrows(ProductNotFoundException.class, () -> movementService.create(request));

        verify(movementRepository, never()).save(any(Movement.class));
        verify(productRepository, never()).save(any(Product.class));
    }

}
