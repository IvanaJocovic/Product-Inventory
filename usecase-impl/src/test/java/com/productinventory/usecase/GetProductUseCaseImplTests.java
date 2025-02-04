package com.productinventory.usecase;

import com.productinventory.domain.gateway.IProductGateway;
import com.productinventory.domain.model.Product;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetProductUseCaseImplTests {

    @Mock
    private IProductGateway productGateway;

    @InjectMocks
    private GetProductUseCaseImpl getProductUseCase;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        EasyRandomParameters parameters = new EasyRandomParameters()
            .randomize(Long.class, () -> (long) (Math.random() * 1000))
            .randomize(Double.class, () -> Math.random() * 500)
            .stringLengthRange(5, 15);

        easyRandom = new EasyRandom(parameters);
    }


    @Test
    void givenEmptyProductList_whenExecuteIsCalled_thenReturnEmptyPage() {

        String nameFilter = easyRandom.nextObject(String.class);
        Double minPrice = 10.0;
        Double maxPrice = 500.0;
        Pageable pageable = PageRequest.of(0, 10);

        when(productGateway.findByNameContainingAndPriceBetween(nameFilter, minPrice, maxPrice, pageable))
            .thenReturn(Page.empty(pageable));

        Page<IGetProductUseCase.GetProductResponse> responsePage = getProductUseCase.execute(nameFilter, minPrice, maxPrice, pageable);

        assertNotNull(responsePage);
        assertTrue(responsePage.isEmpty());

        verify(productGateway, times(1)).findByNameContainingAndPriceBetween(nameFilter, minPrice, maxPrice, pageable);
    }

    @Test
    void givenExtremePriceRange_whenExecuteIsCalled_thenReturnValidPage() {

        String nameFilter = easyRandom.nextObject(String.class);
        Double minPrice = 0.0;
        Double maxPrice = 10000.0;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").descending());

        List<Product> productList = easyRandom.objects(Product.class, 10).collect(Collectors.toList());

        productList.forEach(product -> product.setId(easyRandom.nextLong()));

        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        when(productGateway.findByNameContainingAndPriceBetween(nameFilter, minPrice, maxPrice, pageable))
            .thenReturn(productPage);

        Page<IGetProductUseCase.GetProductResponse> responsePage = getProductUseCase.execute(nameFilter, minPrice, maxPrice, pageable);

        assertNotNull(responsePage);
        assertFalse(responsePage.isEmpty());
        assertEquals(productList.size(), responsePage.getTotalElements());

        verify(productGateway, times(1)).findByNameContainingAndPriceBetween(nameFilter, minPrice, maxPrice, pageable);
    }
}
