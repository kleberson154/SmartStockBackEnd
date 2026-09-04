package com.kleberson.SmartStock.service;

import com.kleberson.SmartStock.dto.product.ProductCreateRequest;
import com.kleberson.SmartStock.dto.product.ProductResponse;
import com.kleberson.SmartStock.dto.product.ProductUpdateRequest;
import com.kleberson.SmartStock.entity.Product;
import com.kleberson.SmartStock.exception.ProductAlreadyExistsException;
import com.kleberson.SmartStock.exception.ProductNotFoundException;
import com.kleberson.SmartStock.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);

        product = new Product();
        product.setName("Teclado");
        product.setCode("TEC-001");
        product.setCategory("Periferico");
        product.setPrice(new BigDecimal("199.90"));
        product.setQuantity(10);
        product.setMinimumStock(3);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID uuid = UUID.randomUUID();

        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.findById(uuid)
        );

        verify(productRepository).findById(uuid);
    }

    @Test
    void shouldThrowExceptionWhenProductCodeAlreadyExists() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Teclado");
        request.setCode("TEC-001");
        request.setCategory("Periferico");
        request.setPrice(new BigDecimal("199.90"));
        request.setQuantity(10);
        request.setMinimumStock(3);

        when(productRepository.findByCode("TEC-001"))
                .thenReturn(Optional.of(product));

        assertThrows(
                ProductAlreadyExistsException.class,
                () -> productService.create(request)
        );

        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldCreateProductSuccessfully() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Teclado");
        request.setCode("TEC-001");
        request.setCategory("Periferico");
        request.setPrice(new BigDecimal("199.90"));
        request.setQuantity(10);
        request.setMinimumStock(3);

        when(productRepository.findByCode("TEC-001"))
                .thenReturn(Optional.empty());

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse createdProduct = productService.create(request);

        assertEquals(request.getName(), createdProduct.getName());
        assertEquals(request.getCode(), createdProduct.getCode());
        assertEquals(request.getCategory(), createdProduct.getCategory());
        assertEquals(request.getPrice(), createdProduct.getPrice());
        assertEquals(request.getQuantity(), createdProduct.getQuantity());
        assertEquals(request.getMinimumStock(), createdProduct.getMinimumStock());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        UUID productId = UUID.randomUUID();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Teclado Mecanico");
        request.setCode("TEC-001");
        request.setCategory("Periferico");
        request.setPrice(new BigDecimal("299.90"));
        request.setMinimumStock(5);

        ProductResponse updatedProduct = productService.update(productId, request);

        assertEquals(request.getName(), updatedProduct.getName());
        assertEquals(request.getCode(), updatedProduct.getCode());
        assertEquals(request.getCategory(), updatedProduct.getCategory());
        assertEquals(request.getPrice(), updatedProduct.getPrice());
        assertEquals(request.getMinimumStock(), updatedProduct.getMinimumStock());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldUpdateProductNotFoundException() {
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Teclado Mecanico");
        request.setCode("TEC-001");
        request.setCategory("Periferico");
        request.setPrice(new BigDecimal("299.90"));
        request.setMinimumStock(5);

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.update(productId, request)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingToExistingCode() {
        UUID productId = UUID.randomUUID();
        product.setId(productId);
        product.setCode("TEC-001");

        Product anotherProduct = new Product();
        anotherProduct.setId(UUID.randomUUID());
        anotherProduct.setCode("MOUSE-001");

        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Teclado Mecanico");
        request.setCode("MOUSE-001");
        request.setCategory("Periferico");
        request.setPrice(new BigDecimal("299.90"));
        request.setMinimumStock(5);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productRepository.findByCode("MOUSE-001"))
                .thenReturn(Optional.of(anotherProduct));

        assertThrows(
                ProductAlreadyExistsException.class,
                () -> productService.update(productId, request)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldAllowUpdateKeepingSameCode() {
        UUID productId = UUID.randomUUID();
        product.setId(productId);
        product.setCode("TEC-001");

        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Teclado Mecanico");
        request.setCode("TEC-001");
        request.setCategory("Periferico");
        request.setPrice(new BigDecimal("299.90"));
        request.setMinimumStock(5);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productRepository.findByCode("TEC-001"))
                .thenReturn(Optional.of(product));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response =
                productService.update(productId, request);

        assertEquals("TEC-001", response.getCode());
        assertEquals("Teclado Mecanico", response.getName());
        assertEquals(15, response.getQuantity());

        verify(productRepository).save(product);
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        UUID productId = UUID.randomUUID();
        product.setId(productId);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        productService.delete(productId);

        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowExceptionWhenDeletingProductNotFound() {
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.delete(productId)
        );

        verify(productRepository, never())
                .delete(any(Product.class));
    }

    @Test
    void shouldReturnLowStockProducts() {
        Product lowStockProduct = new Product();
        lowStockProduct.setId(UUID.randomUUID());
        lowStockProduct.setName("Mouse");
        lowStockProduct.setCode("MOUSE-001");
        lowStockProduct.setCategory("Periferico");
        lowStockProduct.setPrice(new BigDecimal("99.90"));
        lowStockProduct.setQuantity(2);
        lowStockProduct.setMinimumStock(5);

        when(productRepository.findLowStockProducts())
                .thenReturn(List.of(lowStockProduct));

        List<ProductResponse> response =
                productService.findLowStockProducts();

        assertEquals(1, response.size());
        assertEquals("Mouse", response.getFirst().getName());
        assertEquals(2, response.getFirst().getQuantity());
        assertEquals(5, response.getFirst().getMinimumStock());

        verify(productRepository).findLowStockProducts();
    }
}
