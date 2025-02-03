package com.productinventory.domain.gateway;

import com.productinventory.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface IProductGateway {
    Optional<Product> findById(Long id);
    Page<Product> findAll(Pageable pageable);
    Product save(Product product);
    void deleteById(Long id);

    void delete(Product product);
    Page<Product> findByNameContainingAndPriceBetween(String name, Double minPrice, Double maxPrice, Pageable pageable);
}
