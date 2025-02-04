package com.productinventory.usecase;

import java.util.Optional;

public interface IUpdateProductUseCase {
    Optional<UpdateProductResponse> execute(Long id, UpdateProductRequest request);

    class UpdateProductRequest {
        public Integer version;
        public String name;
        public String description;
        public Double price;
        public Integer quantity;

        public UpdateProductRequest(Integer version, String name, String description, Double price, Integer quantity) {
            this.version = version;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
        }
    }

    class UpdateProductResponse {
        public Long id;
        public Integer version;
        public String name;
        public String description;
        public Double price;
        public Integer quantity;

        public UpdateProductResponse(Long id, Integer version, String name, String description, Double price, Integer quantity) {
            this.id = id;
            this.version = version;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
