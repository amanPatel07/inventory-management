package com.inventory_management.mappers;

import com.inventory_management.models.dtos.ProductRequest;
import com.inventory_management.models.dtos.ProductResponse;
import com.inventory_management.models.entity.Product;
import com.inventory_management.models.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        ProductResponse.ProductResponseBuilder responseBuilder = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .currentStock(product.getCurrentStock())
                .minimumStock(product.getMinimumStock());
        if (product.getSupplier() != null) {
            responseBuilder.supplierId(product.getSupplier().getId());
        }
        return responseBuilder.build();
    }

    public Product buildProduct(ProductRequest productRequest, Supplier supplier) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setMinimumStock(productRequest.getMinimumStock());
        product.setSupplier(supplier);
        return product;
    }
}
