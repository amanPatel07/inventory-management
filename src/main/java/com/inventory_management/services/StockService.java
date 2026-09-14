package com.inventory_management.services;

import com.inventory_management.model.dtos.StockMovementRequest;

public interface StockService {

    public void createMovement(StockMovementRequest request);
}
