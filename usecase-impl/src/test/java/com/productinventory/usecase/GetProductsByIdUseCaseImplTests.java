package com.productinventory.usecase;

import com.productinventory.domain.gateway.IProductGateway;
import com.productinventory.domain.model.Product;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetProductsByIdUseCaseImplTests {

    @Mock
    private IProductGateway productGateway;

    @InjectMocks
    private DeleteProductUseCaseImpl deleteProductUseCase;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        easyRandom = new EasyRandom();
    }

    @Test
    void givenValidProductId_whenExecuteIsCalled_thenProductIsDeletedSuccessfully() {

        Long productId = easyRandom.nextLong();
        Product existingProduct = easyRandom.nextObject(Product.class);

        when(productGateway.findById(productId)).thenReturn(Optional.of(existingProduct));

        boolean result = deleteProductUseCase.execute(productId);

        assertTrue(result);
        verify(productGateway, times(1)).delete(existingProduct);
        verify(productGateway, times(1)).findById(productId);
    }

    @Test
    void givenNonExistentProductId_whenExecuteIsCalled_thenReturnFalse() {

        Long productId = easyRandom.nextLong();

        when(productGateway.findById(productId)).thenReturn(Optional.empty());

        boolean result = deleteProductUseCase.execute(productId);

        assertFalse(result);
        verify(productGateway, never()).delete(any(Product.class));
        verify(productGateway, times(1)).findById(productId);
    }
}
