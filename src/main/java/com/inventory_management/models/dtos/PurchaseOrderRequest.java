package com.inventory_management.models.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class PurchaseOrderRequest {
    @NotNull(message = "Supplier ID is required")
    private UUID supplierId;

    @NotEmpty(message = "At least one purchase order item is required")
    private List<PurchaseOrderItemRequest> items;
}
