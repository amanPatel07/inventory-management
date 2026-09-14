package com.inventory_management.services.impl;

import com.inventory_management.model.dtos.ProductRequest;
import com.inventory_management.model.dtos.ProductResponse;
import com.inventory_management.model.entity.Product;
import com.inventory_management.model.entity.Supplier;
import com.inventory_management.repositories.ProductRepository;
import com.inventory_management.repositories.SupplierRepository;
import com.inventory_management.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public ProductServiceImpl(
            ProductRepository productRepository,
            SupplierRepository supplierRepository
    ) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {
        if (request.getSupplierId() == null) {
            throw new IllegalArgumentException("supplierId must not be null");
        }

        Supplier supplier = supplierRepository
                .findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        Product product = new Product();

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setMinimumStock(request.getMinimumStock());

        product.setSupplier(supplier);

        Product savedProduct = productRepository.save(product);

        return toResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setCurrentStock(product.getCurrentStock());
        response.setMinimumStock(product.getMinimumStock());
        if (product.getSupplier() != null) {
            response.setSupplierId(product.getSupplier().getId());
        }

        return response;
    }
}
