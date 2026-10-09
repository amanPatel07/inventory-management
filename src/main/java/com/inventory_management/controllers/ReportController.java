package com.inventory_management.controllers;

import com.inventory_management.model.dtos.ProductResponse;
import com.inventory_management.model.dtos.InventorySummaryResponse;
import com.inventory_management.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('REPORT_READ')")
    @GetMapping("/low-stock")
    public List<ProductResponse> getLowStockProducts() {
        return reportService.getLowStockProducts();
    }

    @PreAuthorize("hasAuthority('REPORT_READ')")
    @GetMapping("/stock-value")
    public double getTotalStockValue() {
        return reportService.getTotalStockValue();
    }

    @PreAuthorize("hasAuthority('REPORT_READ')")
    @GetMapping("/stock-value/category")
    public Map<String, Double> getStockValueByCategory() {
        return reportService.getStockValueByCategory();
    }

    @Operation(summary = "Get inventory summary")
    @PreAuthorize("hasAuthority('REPORT_READ')")
    @GetMapping("/summary")
    public InventorySummaryResponse getInventorySummary() {
        return reportService.getInventorySummary();
    }
}
