package com.productinventory.usecase;

import com.productinventory.domain.gateway.IProductGateway;
import com.productinventory.domain.model.Product;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateProductUseCaseImpl implements IUpdateProductUseCase {

    private final IProductGateway productGateway;

    public UpdateProductUseCaseImpl(IProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    @Transactional
    public Optional<UpdateProductResponse> execute(Long id, UpdateProductRequest request) {
        return productGateway.findById(id).map(existingProduct -> {
            if (!existingProduct.getVersion().equals(request.version)) {
                throw new OptimisticLockException("Product version mismatch. Please refresh and try again.");
            }

            existingProduct.setName(request.name);
            existingProduct.setDescription(request.description);
            existingProduct.setPrice(request.price);
            existingProduct.setQuantity(request.quantity);

            Product updatedProduct = productGateway.save(existingProduct);

            return new UpdateProductResponse(
                updatedProduct.getId(),
                updatedProduct.getVersion(),
                updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getPrice(),
                updatedProduct.getQuantity()
            );
        });
    }
}
