package com.productinventory.persistence.repository;

import com.productinventory.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingAndPriceBetween(String name, Double minPrice, Double maxPrice, Pageable pageable);
}

