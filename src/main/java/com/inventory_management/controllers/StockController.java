package com.inventory_management.controllers;

import com.inventory_management.model.dtos.StockMovementRequest;
import com.inventory_management.model.dtos.StockMovementResponse;
import com.inventory_management.model.enums.StockMovementType;
import com.inventory_management.services.StockService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/movement")
    public ResponseEntity<Void> createMovement(@RequestBody @Valid StockMovementRequest request) {
        stockService.createMovement(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get stock movement history",
            description = "Returns stock movement history with optional product and type filters. Date filtering requires both from and to.")
    @GetMapping("/movements")
    public List<StockMovementResponse> getMovements(
            @Parameter(description = "Filter by product UUID")
            @RequestParam(required = false) UUID productId,
            @Parameter(description = "Filter by movement type", example = "OUT")
            @RequestParam(required = false) StockMovementType type,
            @Parameter(description = "Start date in yyyy-MM-dd format", example = "2026-09-01")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date in yyyy-MM-dd format", example = "2026-09-15")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return stockService.getMovements(productId, type, from, to);
    }
}
