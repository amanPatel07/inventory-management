package com.inventory_management.services;

import com.inventory_management.model.dtos.ProductRequest;
import com.inventory_management.model.dtos.ProductResponse;
import com.inventory_management.model.entity.Product;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest product);

    List<ProductResponse> getAllProducts();
}
