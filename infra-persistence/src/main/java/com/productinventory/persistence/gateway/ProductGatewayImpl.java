package com.productinventory.persistence.gateway;

import com.productinventory.domain.gateway.IProductGateway;
import com.productinventory.domain.model.Product;
import com.productinventory.persistence.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductGatewayImpl implements IProductGateway {

    private final ProductRepository productRepository;

    public ProductGatewayImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll((org.springframework.data.domain.Pageable) pageable);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public Page<Product> findByNameContainingAndPriceBetween(String name, Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.findByNameContainingAndPriceBetween(name, minPrice, maxPrice, pageable);
    }
}
