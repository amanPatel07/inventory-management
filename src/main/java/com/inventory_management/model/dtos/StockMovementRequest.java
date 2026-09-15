package com.inventory_management.model.dtos;

import com.inventory_management.model.enums.StockMovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class StockMovementRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private StockMovementType type;

    @Positive
    private int quantity;

    private String reason;
}
