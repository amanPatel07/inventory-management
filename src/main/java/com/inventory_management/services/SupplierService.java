package com.inventory_management.services;

import com.inventory_management.model.dtos.SupplierRequest;
import com.inventory_management.model.dtos.SupplierResponse;
import com.inventory_management.model.entity.Supplier;
import com.inventory_management.repositories.SupplierRepository;

import java.util.List;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierRequest supplier);

    List<SupplierResponse> getSuppliers();
}
