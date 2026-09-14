package com.inventory_management.controllers;

import com.inventory_management.model.dtos.StockMovementRequest;
import com.inventory_management.services.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/movement")
    public ResponseEntity<Void> createMovement(@RequestBody StockMovementRequest request) {
        stockService.createMovement(request);
        return ResponseEntity.ok().build();
    }
}
