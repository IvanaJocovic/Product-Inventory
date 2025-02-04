package com.productinventory.usecase;

import com.productinventory.domain.gateway.IProductGateway;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DeleteProductUseCaseImpl implements IDeleteProductUseCase {

    private final IProductGateway productGateway;

    public DeleteProductUseCaseImpl(IProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    @Transactional
    public boolean execute(Long id) {
        return productGateway.findById(id).map(product -> {
            productGateway.delete(product);
            return true;
        }).orElse(false);
    }
}
