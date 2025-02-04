package com.productinventory.usecase;

import com.productinventory.domain.gateway.IProductGateway;
import com.productinventory.domain.model.Product;
import jakarta.persistence.OptimisticLockException;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateProductUseCaseImplTests {

    @Mock
    private IProductGateway productGateway;

    @InjectMocks
    private UpdateProductUseCaseImpl updateProductUseCase;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        EasyRandomParameters parameters = new EasyRandomParameters()
            .randomize(Long.class, () -> (long) (Math.random() * 1000))
            .randomize(Integer.class, () -> (int) (Math.random() * 10))
            .stringLengthRange(5, 15);

        easyRandom = new EasyRandom(parameters);
    }

    @Test
    void givenValidProductIdAndMatchingVersion_whenExecuteIsCalled_thenUpdateProductSuccessfully() {

        Long productId = easyRandom.nextLong();
        Product existingProduct = easyRandom.nextObject(Product.class);
        existingProduct.setId(productId);

        IUpdateProductUseCase.UpdateProductRequest updateRequest =
            easyRandom.nextObject(IUpdateProductUseCase.UpdateProductRequest.class);
        updateRequest.version = existingProduct.getVersion();

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setVersion(existingProduct.getVersion());
        updatedProduct.setName(updateRequest.name);
        updatedProduct.setDescription(updateRequest.description);
        updatedProduct.setPrice(updateRequest.price);
        updatedProduct.setQuantity(updateRequest.quantity);

        when(productGateway.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productGateway.save(existingProduct)).thenReturn(updatedProduct);

        Optional<IUpdateProductUseCase.UpdateProductResponse> response = updateProductUseCase.execute(productId, updateRequest);

        assertTrue(response.isPresent());
        assertEquals(updatedProduct.getId(), response.get().id);
        assertEquals(updatedProduct.getVersion(), response.get().version);
        assertEquals(updatedProduct.getName(), response.get().name);
        assertEquals(updatedProduct.getDescription(), response.get().description);
        assertEquals(updatedProduct.getPrice(), response.get().price);
        assertEquals(updatedProduct.getQuantity(), response.get().quantity);

        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, times(1)).save(existingProduct);
    }

    @Test
    void givenNonExistentProductId_whenExecuteIsCalled_thenReturnEmptyOptional() {

        Long productId = easyRandom.nextLong();
        IUpdateProductUseCase.UpdateProductRequest updateRequest =
            easyRandom.nextObject(IUpdateProductUseCase.UpdateProductRequest.class);

        when(productGateway.findById(productId)).thenReturn(Optional.empty());

        Optional<IUpdateProductUseCase.UpdateProductResponse> response = updateProductUseCase.execute(productId, updateRequest);

        assertFalse(response.isPresent());

        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, never()).save(any());
    }

    @Test
    void givenVersionMismatch_whenExecuteIsCalled_thenThrowOptimisticLockException() {

        Long productId = easyRandom.nextLong();
        Product existingProduct = easyRandom.nextObject(Product.class);
        existingProduct.setId(productId);

        IUpdateProductUseCase.UpdateProductRequest updateRequest =
            easyRandom.nextObject(IUpdateProductUseCase.UpdateProductRequest.class);
        updateRequest.version = existingProduct.getVersion() + 1;

        when(productGateway.findById(productId)).thenReturn(Optional.of(existingProduct));

        OptimisticLockException thrownException = assertThrows(OptimisticLockException.class, () ->
            updateProductUseCase.execute(productId, updateRequest)
        );

        assertEquals("Product version mismatch. Please refresh and try again.", thrownException.getMessage());

        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, never()).save(any());
    }
}
