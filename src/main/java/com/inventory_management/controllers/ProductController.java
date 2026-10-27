package com.inventory_management.controllers;

import com.inventory_management.models.dtos.*;
import com.inventory_management.services.ProductService;
import com.inventory_management.services.PurchaseOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final PurchaseOrderService purchaseOrderService;

    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    @PostMapping
    public ProductResponse createProduct(@RequestBody @NotNull @Valid ProductRequest product) {
        return productService.createProduct(product);
    }

    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasAuthority('CREATE_PURCHASE_ORDER')")
    @PostMapping("/create-order")
    public PurchaseOrderResponse createPurchaseOrder(@RequestBody @NotNull @Valid PurchaseOrderRequest purchaseOrderRequest) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderRequest);
    }

    @PreAuthorize("hasAuthority('CREATE_PURCHASE_ORDER')")
    @PostMapping("/{purchaseOrderId}/submit")
    public ResponseEntity<ApiResponse<Void>> submitForApproval(
            @PathVariable(required = true) UUID purchaseOrderId
    ) {
        purchaseOrderService.sendPurchaseOrderForApproval(purchaseOrderId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Purchase order submitted for approval",
                        null
                ));
    }

    @PreAuthorize("hasAuthority('CREATE_PURCHASE_ORDER')")
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponse>>> getOrders() {
        List<PurchaseOrderResponse> purchaseOrderResponseList = purchaseOrderService.getAllPurchaseOrder();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "success",
                        purchaseOrderResponseList
                ));
    }
}
