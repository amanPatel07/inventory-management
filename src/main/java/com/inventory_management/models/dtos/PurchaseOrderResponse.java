package com.inventory_management.models.dtos;

import com.inventory_management.models.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Setter
@Getter
public class PurchaseOrderResponse {
    private UUID id;
    private UUID supplierId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<PurchaseOrderItemResponse> items;
}
