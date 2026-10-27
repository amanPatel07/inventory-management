package com.inventory_management.mappers;

import com.inventory_management.models.dtos.PurchaseOrderItemResponse;
import com.inventory_management.models.dtos.PurchaseOrderResponse;
import com.inventory_management.models.entity.PurchaseOrder;
import com.inventory_management.models.entity.PurchaseOrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseOrderMapper {

    public PurchaseOrderResponse toResponse(PurchaseOrder purchaseOrder) {
        List<PurchaseOrderItemResponse> items =
                purchaseOrder.getItems().stream()
                        .map(this::toItemResponse)
                        .toList();
        PurchaseOrderResponse.PurchaseOrderResponseBuilder purchaseOrderResponseBuilder = PurchaseOrderResponse.builder()
                .id(purchaseOrder.getId())
                .orderDate(purchaseOrder.getCreatedAt())
                .supplierId(purchaseOrder.getSupplier().getId())
                .status(purchaseOrder.getStatus())
                .items(items);
        return purchaseOrderResponseBuilder.build();
    }

    private PurchaseOrderItemResponse toItemResponse(PurchaseOrderItem purchaseOrderItem) {
        return PurchaseOrderItemResponse.builder()
                .id(purchaseOrderItem.getId())
                .productId(purchaseOrderItem.getProduct().getId())
                .orderedQuantity(purchaseOrderItem.getOrderedQuantity())
                .receivedQuantity(purchaseOrderItem.getReceivedQuantity())
                .pendingQuantity(purchaseOrderItem.getOrderedQuantity() - purchaseOrderItem.getReceivedQuantity())
                .productName(purchaseOrderItem.getProduct().getName())
                .unitPrice(purchaseOrderItem.getPrice())
                .build();
    }
}
