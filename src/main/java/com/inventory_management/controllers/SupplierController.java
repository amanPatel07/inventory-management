package com.inventory_management.controllers;

import com.inventory_management.model.dtos.SupplierRequest;
import com.inventory_management.model.dtos.SupplierResponse;
import com.inventory_management.services.SupplierService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public SupplierResponse createSupplier(@RequestBody @Valid SupplierRequest request) {
        return supplierService.createSupplier(request);
    }

    @GetMapping
    public List<SupplierResponse> getSuppliers() {
        return supplierService.getSuppliers();
    }
}
