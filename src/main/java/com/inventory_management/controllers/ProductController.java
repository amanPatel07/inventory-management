package com.inventory_management.controllers;

import com.inventory_management.model.dtos.ProductRequest;
import com.inventory_management.model.dtos.ProductResponse;
import com.inventory_management.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody @NotNull @Valid ProductRequest product) {
        return productService.createProduct(product);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

}
