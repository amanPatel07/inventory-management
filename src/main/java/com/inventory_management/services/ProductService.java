package com.inventory_management.services;

import com.inventory_management.models.dtos.ProductRequest;
import com.inventory_management.models.dtos.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest product);

    List<ProductResponse> getAllProducts();
}
