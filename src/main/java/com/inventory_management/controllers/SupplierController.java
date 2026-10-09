package com.inventory_management.controllers;

import com.inventory_management.models.dtos.SupplierRequest;
import com.inventory_management.models.dtos.SupplierResponse;
import com.inventory_management.services.SupplierService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SupplierResponse createSupplier(@RequestBody @Valid SupplierRequest request) {
        return supplierService.createSupplier(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_USER')")
    @GetMapping
    public List<SupplierResponse> getSuppliers() {
        return supplierService.getSuppliers();
    }
}
