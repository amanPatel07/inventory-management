package com.inventory_management.services.impl;

import com.inventory_management.exception.InsufficientStockException;
import com.inventory_management.exception.ResourceNotFoundException;
import com.inventory_management.model.dtos.StockMovementRequest;
import com.inventory_management.model.entity.Product;
import com.inventory_management.model.entity.StockMovement;
import com.inventory_management.model.enums.StockMovementType;
import com.inventory_management.repositories.ProductRepository;
import com.inventory_management.repositories.StockMovementRepository;
import com.inventory_management.services.StockService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StockServiceImpl implements StockService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public StockServiceImpl(
            ProductRepository productRepository,
            StockMovementRepository stockMovementRepository
    ) {
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public void createMovement(StockMovementRequest request) {

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found: " + request.getProductId()
                ));

        if (request.getType() == StockMovementType.IN) {
            product.setCurrentStock(product.getCurrentStock() + request.getQuantity());
        } else if (request.getType() == StockMovementType.OUT) {
            if (request.getQuantity() > product.getCurrentStock()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName()
                );
            }
            product.setCurrentStock(product.getCurrentStock() - request.getQuantity());
        }

        productRepository.save(product);

        StockMovement movement = new StockMovement();

        movement.setProduct(product);
        movement.setType(request.getType());
        movement.setQuantity(request.getQuantity());
        movement.setReason(request.getReason());
        movement.setCreatedAt(LocalDateTime.now());

        stockMovementRepository.save(movement);
    }

}
