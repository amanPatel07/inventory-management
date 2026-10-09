package com.inventory_management.mappers;

import com.inventory_management.models.dtos.StockMovementResponse;
import com.inventory_management.models.entity.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {

    public StockMovementResponse toResponse(StockMovement movement) {
        StockMovementResponse.StockMovementResponseBuilder stockMovementResponseBuilder = StockMovementResponse.builder();
        stockMovementResponseBuilder.id(movement.getId())
                .productId(movement.getProduct().getId())
                .productName(movement.getProduct().getName())
                .type(movement.getType())
                .quantity(movement.getQuantity())
                .reason(movement.getReason())
                .createdAt(movement.getCreatedAt());
        return stockMovementResponseBuilder.build();
    }
}
