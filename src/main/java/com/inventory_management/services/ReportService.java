package com.inventory_management.services;

import com.inventory_management.model.dtos.ProductResponse;
import com.inventory_management.model.dtos.InventorySummaryResponse;

import java.util.List;
import java.util.Map;

public interface ReportService {

    public List<ProductResponse> getLowStockProducts();

    public double getTotalStockValue();

    public Map<String, Double> getStockValueByCategory();

    InventorySummaryResponse getInventorySummary();
}
