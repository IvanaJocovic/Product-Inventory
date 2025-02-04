package com.productinventory.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface IGetProductUseCase {
    Page<GetProductResponse> execute(String name, Double minPrice, Double maxPrice, Pageable pageable);

    class GetProductResponse {
        public Long id;
        public String name;
        public String description;
        public Double price;
        public Integer quantity;

        public GetProductResponse(Long id, String name, String description, Double price, Integer quantity) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
