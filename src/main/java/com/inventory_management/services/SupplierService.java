package com.inventory_management.services;

import com.inventory_management.models.dtos.SupplierRequest;
import com.inventory_management.models.dtos.SupplierResponse;

import java.util.List;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierRequest supplier);

    List<SupplierResponse> getSuppliers();
}
