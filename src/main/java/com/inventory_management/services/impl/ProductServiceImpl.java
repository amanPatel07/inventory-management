package com.inventory_management.services.impl;

import com.inventory_management.mappers.ProductMapper;
import com.inventory_management.models.dtos.ProductRequest;
import com.inventory_management.models.dtos.ProductResponse;
import com.inventory_management.models.entity.Product;
import com.inventory_management.models.entity.Supplier;
import com.inventory_management.exceptions.ResourceNotFoundException;
import com.inventory_management.repositories.ProductRepository;
import com.inventory_management.repositories.SupplierRepository;
import com.inventory_management.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest request) {
        if (request.getSupplierId() == null) {
            throw new IllegalArgumentException("supplierId must not be null");
        }
        Supplier supplier = supplierRepository
                .findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Supplier not found: " + request.getSupplierId()));
        Product product = productMapper.buildProduct(request, supplier);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }
}
