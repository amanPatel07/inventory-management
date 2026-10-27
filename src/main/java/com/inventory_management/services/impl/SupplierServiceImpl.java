package com.inventory_management.services.impl;

import com.inventory_management.mappers.SupplierMapper;
import com.inventory_management.models.dtos.SupplierRequest;
import com.inventory_management.models.dtos.SupplierResponse;
import com.inventory_management.models.entity.Supplier;
import com.inventory_management.repositories.SupplierRepository;
import com.inventory_management.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierResponse createSupplier(SupplierRequest request) {

        Supplier supplier = new Supplier();

        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toResponse(savedSupplier);
    }

    public List<SupplierResponse> getSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toResponse)
                .toList();
    }
}
