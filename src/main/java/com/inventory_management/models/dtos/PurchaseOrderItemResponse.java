package com.inventory_management.models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
public class PurchaseOrderItemResponse {
    private UUID id;
    private UUID productId;
    private String productName;
    private Integer orderedQuantity;
    private Integer receivedQuantity;
    private Integer pendingQuantity;
    private BigDecimal unitPrice;
}
