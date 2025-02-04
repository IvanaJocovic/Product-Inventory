package com.productinventory.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productinventory.usecase.*;
import com.productinventory.web.controller.ProductController;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ICreateProductUseCase createProductUseCase;

    @Mock
    private IGetProductUseCase getProductUseCase;

    @Mock
    private IGetProductsByIdUseCase getProductsByIdUseCase;

    @Mock
    private IUpdateProductUseCase updateProductUseCase;

    @Mock
    private IDeleteProductUseCase deleteProductUseCase;

    @InjectMocks
    private ProductController productController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        easyRandom = new EasyRandom();
    }

    @Test
    void givenValidRequest_whenCreateProduct_thenReturnsCreatedProduct() throws Exception {

        ICreateProductUseCase.CreateProductRequest request = easyRandom.nextObject(ICreateProductUseCase.CreateProductRequest.class);
        request.setName("Valid Product");
        request.setDescription("Valid Description");
        request.setPrice(99.99);
        request.setQuantity(5);

        ICreateProductUseCase.CreateProductResponse response = easyRandom.nextObject(ICreateProductUseCase.CreateProductResponse.class);

        when(createProductUseCase.execute(any(ICreateProductUseCase.CreateProductRequest.class))).thenReturn(response);

        System.out.println("Request JSON: " + objectMapper.writeValueAsString(request));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(response.getId()))
            .andExpect(jsonPath("$.name").value(response.getName()));

        verify(createProductUseCase, times(1)).execute(any(ICreateProductUseCase.CreateProductRequest.class));
    }

    @Test
    void givenExistingProductId_whenGetProductById_thenReturnsProduct() throws Exception {

        Long productId = easyRandom.nextLong();
        IGetProductsByIdUseCase.GetProductsByIdResponse response = easyRandom.nextObject(IGetProductsByIdUseCase.GetProductsByIdResponse.class);

        when(getProductsByIdUseCase.execute(productId)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/products/{id}", productId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(response.id));

        verify(getProductsByIdUseCase, times(1)).execute(productId);
    }

    @Test
    void givenNonExistingProductId_whenGetProductById_thenReturnsNotFound() throws Exception {

        Long productId = easyRandom.nextLong();

        when(getProductsByIdUseCase.execute(productId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/{id}", productId))
            .andExpect(status().isNotFound());

        verify(getProductsByIdUseCase, times(1)).execute(productId);
    }

    @Test
    void givenValidUpdateRequest_whenUpdateProduct_thenReturnsUpdatedProduct() throws Exception {

        Long productId = easyRandom.nextLong();
        IUpdateProductUseCase.UpdateProductRequest request = easyRandom.nextObject(IUpdateProductUseCase.UpdateProductRequest.class);
        IUpdateProductUseCase.UpdateProductResponse response = easyRandom.nextObject(IUpdateProductUseCase.UpdateProductResponse.class);

        when(updateProductUseCase.execute(eq(productId), any(IUpdateProductUseCase.UpdateProductRequest.class)))
            .thenReturn(Optional.of(response));

        mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(response.id));

        verify(updateProductUseCase, times(1)).execute(eq(productId), any(IUpdateProductUseCase.UpdateProductRequest.class));
    }

    @Test
    void givenNonExistingProductId_whenUpdateProduct_thenReturnsNotFound() throws Exception {

        Long productId = easyRandom.nextLong();
        IUpdateProductUseCase.UpdateProductRequest request = easyRandom.nextObject(IUpdateProductUseCase.UpdateProductRequest.class);

        when(updateProductUseCase.execute(eq(productId), any(IUpdateProductUseCase.UpdateProductRequest.class)))
            .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());

        verify(updateProductUseCase, times(1)).execute(eq(productId), any(IUpdateProductUseCase.UpdateProductRequest.class));
    }

    @Test
    void givenExistingProductId_whenDeleteProduct_thenReturnsNoContent() throws Exception {

        Long productId = easyRandom.nextLong();
        when(deleteProductUseCase.execute(productId)).thenReturn(true);

        mockMvc.perform(delete("/api/products/{id}", productId))
            .andExpect(status().isNoContent());

        verify(deleteProductUseCase, times(1)).execute(productId);
    }

    @Test
    void givenNonExistingProductId_whenDeleteProduct_thenReturnsNotFound() throws Exception {

        Long productId = easyRandom.nextLong();
        when(deleteProductUseCase.execute(productId)).thenReturn(false);

        mockMvc.perform(delete("/api/products/{id}", productId))
            .andExpect(status().isNotFound());

        verify(deleteProductUseCase, times(1)).execute(productId);
    }

    @Test
    void givenInvalidQueryParamType_whenGetProducts_thenReturnsBadRequest() throws Exception {

        mockMvc.perform(get("/api/products")
                .param("minPrice", "abc")
                .param("maxPrice", "xyz")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(getProductUseCase, never()).execute(any(), any(), any(), any());
    }

}
