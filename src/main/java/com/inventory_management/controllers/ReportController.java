package com.inventory_management.controllers;

import com.inventory_management.model.dtos.ProductResponse;
import com.inventory_management.services.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/low-stock")
    public List<ProductResponse> getLowStockProducts() {
        return reportService.getLowStockProducts();
    }

    @GetMapping("/stock-value")
    public double getTotalStockValue() {
        return reportService.getTotalStockValue();
    }

    @GetMapping("/stock-value/category")
    public Map<String, Double> getStockValueByCategory() {
        return reportService.getStockValueByCategory();
    }
}
