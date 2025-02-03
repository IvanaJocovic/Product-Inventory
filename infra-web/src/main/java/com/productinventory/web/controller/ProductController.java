package com.productinventory.web.controller;

import com.productinventory.usecase.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ICreateProductUseCase createProductUseCase;
    private final IGetProductUseCase getProductUseCase;
    private final IGetProductsByIdUseCase getProductsByIdUseCase;
    private final IUpdateProductUseCase updateProductUseCase;
    private final IDeleteProductUseCase deleteProductUseCase;

    public ProductController(
        ICreateProductUseCase createProductUseCase,
        IGetProductUseCase getProductUseCase,
        IGetProductsByIdUseCase getProductsByIdUseCase,
        IUpdateProductUseCase updateProductUseCase,
        IDeleteProductUseCase deleteProductUseCase
    ) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.getProductsByIdUseCase = getProductsByIdUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @PostMapping
    public ResponseEntity<ICreateProductUseCase.CreateProductResponse> createProduct(
        @Valid @RequestBody ICreateProductUseCase.CreateProductRequest request) {
        return ResponseEntity.ok(createProductUseCase.execute(request));
    }

    @GetMapping
    public ResponseEntity<Page<IGetProductUseCase.GetProductResponse>> getProducts(
        @RequestParam(required = false, defaultValue = "") String name,
        @RequestParam(required = false, defaultValue = "0") Double minPrice,
        @RequestParam(required = false, defaultValue = "99999999") Double maxPrice,
        Pageable pageable) {

        return ResponseEntity.ok(getProductUseCase.execute(name, minPrice, maxPrice, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IGetProductsByIdUseCase.GetProductsByIdResponse> getProductsById(@PathVariable Long id) {
        Optional<IGetProductsByIdUseCase.GetProductsByIdResponse> product = getProductsByIdUseCase.execute(id);
        return product.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IUpdateProductUseCase.UpdateProductResponse> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody IUpdateProductUseCase.UpdateProductRequest request) {

        Optional<IUpdateProductUseCase.UpdateProductResponse> updatedProduct = updateProductUseCase.execute(id, request);

        return updatedProduct.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = deleteProductUseCase.execute(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
