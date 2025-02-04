package com.productinventory.usecase;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface ICreateProductUseCase {

    CreateProductResponse execute(CreateProductRequest request);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateProductRequest {
        @NotBlank(message = "Name is required")
        private String name;

        private String description;

        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price must be a positive value")
        private Double price;

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity must be a positive value")
        private Integer quantity;
    }

    @Data
    @AllArgsConstructor
    class CreateProductResponse {
        private Long id;
        private String name;
        private String description;
        private Double price;
        private Integer quantity;
    }
}
