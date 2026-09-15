package com.inventory_management.model.dtos;

import com.inventory_management.model.enums.StockMovementType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class StockMovementResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private StockMovementType type;
    private int quantity;
    private String reason;
    private LocalDateTime createdAt;
}