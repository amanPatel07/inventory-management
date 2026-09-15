package com.inventory_management.services;

import com.inventory_management.model.dtos.StockMovementRequest;
import com.inventory_management.model.dtos.StockMovementResponse;
import com.inventory_management.model.enums.StockMovementType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StockService {

    public void createMovement(StockMovementRequest request);

    List<StockMovementResponse> getMovements(
            UUID productId,
            StockMovementType type,
            LocalDate from,
            LocalDate to);
}
