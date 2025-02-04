package com.productinventory.usecase;

import com.productinventory.domain.gateway.IProductGateway;
import com.productinventory.domain.model.Product;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateProductUseCaseImplTests {

    @Mock
    private IProductGateway productGateway;

    @InjectMocks
    private CreateProductUseCaseImpl createProductUseCase;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        easyRandom = new EasyRandom();
    }

    @Test
    void givenValidInput_whenExecuteIsCalled_thenProductIsCreatedSuccessfully() {

        ICreateProductUseCase.CreateProductRequest request = easyRandom.nextObject(
            ICreateProductUseCase.CreateProductRequest.class
        );

        Product productToSave = new Product();
        productToSave.setName(request.getName());
        productToSave.setDescription(request.getDescription());
        productToSave.setPrice(request.getPrice());
        productToSave.setQuantity(request.getQuantity());

        Product savedProduct = new Product();
        savedProduct.setId(easyRandom.nextLong());
        savedProduct.setName(productToSave.getName());
        savedProduct.setDescription(productToSave.getDescription());
        savedProduct.setPrice(productToSave.getPrice());
        savedProduct.setQuantity(productToSave.getQuantity());

        when(productGateway.save(any(Product.class))).thenReturn(savedProduct);
        ICreateProductUseCase.CreateProductResponse response = createProductUseCase.execute(request);

        assertNotNull(response);
        assertEquals(savedProduct.getId(), response.getId());
        assertEquals(savedProduct.getName(), response.getName());
        assertEquals(savedProduct.getDescription(), response.getDescription());
        assertEquals(savedProduct.getPrice(), response.getPrice());
        assertEquals(savedProduct.getQuantity(), response.getQuantity());

        verify(productGateway, times(1)).save(any(Product.class));
    }

    @Test
    void givenProductSaveFails_whenExecuteIsCalled_thenThrowRuntimeException() {

        ICreateProductUseCase.CreateProductRequest request =
            easyRandom.nextObject(ICreateProductUseCase.CreateProductRequest.class);

        when(productGateway.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> createProductUseCase.execute(request));

        verify(productGateway, times(1)).save(any(Product.class));
    }
}
