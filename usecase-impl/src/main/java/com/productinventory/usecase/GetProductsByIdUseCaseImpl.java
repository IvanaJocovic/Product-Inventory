package com.productinventory.usecase;

import com.productinventory.domain.gateway.IProductGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetProductsByIdUseCaseImpl implements IGetProductsByIdUseCase{

    private final IProductGateway productGateway;

    public GetProductsByIdUseCaseImpl(IProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public Optional<GetProductsByIdResponse> execute(Long id) {
        return productGateway.findById(id)
            .map(product -> new GetProductsByIdResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
            ));
    }
}
