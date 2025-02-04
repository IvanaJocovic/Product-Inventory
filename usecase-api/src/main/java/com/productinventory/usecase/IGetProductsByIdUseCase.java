package com.productinventory.usecase;

import java.util.Optional;

public interface IGetProductsByIdUseCase {
    Optional<GetProductsByIdResponse> execute(Long id);

    class GetProductsByIdResponse {
        public Long id;
        public String name;
        public String description;
        public Double price;
        public Integer quantity;

        public GetProductsByIdResponse(Long id, String name, String description, Double price, Integer quantity) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
