package com.inventory_management.model.dtos;

import com.inventory_management.model.enums.StockMovementType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class StockMovementRequest {

    private UUID productId;
    private StockMovementType type;
    private int quantity;
    private String reason;
}
