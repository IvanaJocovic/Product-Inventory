package com.productinventory.usecase;

import com.productinventory.domain.gateway.IProductGateway;
import com.productinventory.domain.model.Product;
import org.springframework.stereotype.Service;

@Service
public class CreateProductUseCaseImpl implements ICreateProductUseCase{

    private final IProductGateway productGateway;

    public CreateProductUseCaseImpl(IProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public CreateProductResponse execute(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        Product savedProduct = productGateway.save(product);

        return new CreateProductResponse(
            savedProduct.getId(),
            savedProduct.getName(),
            savedProduct.getDescription(),
            savedProduct.getPrice(),
            savedProduct.getQuantity()
        );
    }
}
