package com.inventory_management.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventorySummaryResponse {

    private long totalProducts;
    private long totalUnitsInStock;
    private long lowStockProducts;
    private double totalInventoryValue;
    private long totalStockIn;
    private long totalStockOut;
}