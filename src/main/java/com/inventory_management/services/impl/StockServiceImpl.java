package com.inventory_management.services.impl;

import com.inventory_management.exception.InsufficientStockException;
import com.inventory_management.exception.ResourceNotFoundException;
import com.inventory_management.model.dtos.StockMovementRequest;
import com.inventory_management.model.dtos.StockMovementResponse;
import com.inventory_management.model.entity.Product;
import com.inventory_management.model.entity.StockMovement;
import com.inventory_management.model.enums.StockMovementType;
import com.inventory_management.repositories.ProductRepository;
import com.inventory_management.repositories.StockMovementRepository;
import com.inventory_management.services.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Transactional
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

    @Override
    public List<StockMovementResponse> getMovements(
            UUID productId,
            StockMovementType type,
            LocalDate from,
            LocalDate to) {
        if ((from == null) != (to == null)) {
            throw new IllegalArgumentException("Both from and to dates must be provided together");
        }

        if (from != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date cannot be after to date");
        }

        LocalDateTime fromDateTime = from == null ? null : from.atStartOfDay();
        LocalDateTime toDateTime = to == null ? null : to.plusDays(1).atStartOfDay();

        List<StockMovement> movements = stockMovementRepository
            .findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
            .stream()
            .filter(movement -> productId == null
                || movement.getProduct().getId().equals(productId))
            .filter(movement -> type == null || movement.getType() == type)
            .filter(movement -> fromDateTime == null
                || !movement.getCreatedAt().isBefore(fromDateTime))
            .filter(movement -> toDateTime == null
                || movement.getCreatedAt().isBefore(toDateTime))
            .toList();

        return movements
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private StockMovementResponse toResponse(StockMovement movement) {
        StockMovementResponse response = new StockMovementResponse();
        response.setId(movement.getId());
        response.setProductId(movement.getProduct().getId());
        response.setProductName(movement.getProduct().getName());
        response.setType(movement.getType());
        response.setQuantity(movement.getQuantity());
        response.setReason(movement.getReason());
        response.setCreatedAt(movement.getCreatedAt());
        return response;
    }

}
