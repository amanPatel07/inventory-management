package com.inventory_management.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class PurchaseOrderItemRequest {
    @NotBlank(message = "Product ID is required")
    private UUID productId;

    @NotBlank(message = "Ordered Quantity is required")
    @Positive(message = "Ordered quantity must be greater than 0")
    private Integer orderQuantity;

    @NotBlank(message = "Unit Price is required")
    @Positive(message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;
}
