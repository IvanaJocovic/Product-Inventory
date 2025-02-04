package com.productinventory.usecase;

import com.productinventory.domain.gateway.IProductGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetProductUseCaseImpl implements IGetProductUseCase {

    private final IProductGateway productGateway;

    public GetProductUseCaseImpl(IProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public Page<GetProductResponse> execute(String name, Double minPrice, Double maxPrice, Pageable pageable) {
        return productGateway.findByNameContainingAndPriceBetween(name, minPrice, maxPrice, pageable)
            .map(product -> new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
            ));
    }
}
