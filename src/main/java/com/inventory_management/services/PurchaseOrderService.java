package com.inventory_management.services;

import com.inventory_management.exceptions.PurchaseOrderIllegalStatusException;
import com.inventory_management.exceptions.ResourceNotFoundException;
import com.inventory_management.mappers.PurchaseOrderMapper;
import com.inventory_management.models.dtos.PurchaseOrderRequest;
import com.inventory_management.models.dtos.PurchaseOrderResponse;
import com.inventory_management.models.entity.*;
import com.inventory_management.models.enums.OrderStatus;
import com.inventory_management.repositories.ProductRepository;
import com.inventory_management.repositories.PurchaseOrderRepository;
import com.inventory_management.repositories.SupplierRepository;
import com.inventory_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest purchaseOrderRequest) {
        Supplier supplier = supplierRepository.findById(purchaseOrderRequest.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with the id " + purchaseOrderRequest.getSupplierId()));
        UUID userId = userService.getCurrentUserId();
        User currentUser = userRepository.getReferenceById(userId);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setCreatedAt(LocalDateTime.now());
        purchaseOrder.setStatus(OrderStatus.DRAFT);
        purchaseOrder.setCreatedBy(currentUser);
        purchaseOrderRequest.getItems().forEach((item) -> {
            Product foundProduct = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("No product found with id " + item.getProductId()));
            PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
            purchaseOrderItem.setPrice(item.getUnitPrice());
            purchaseOrderItem.setOrderedQuantity(item.getOrderQuantity());
            purchaseOrderItem.setProduct(foundProduct);
            purchaseOrder.addItem(purchaseOrderItem);
        });
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toResponse(savedPurchaseOrder);
    }

    @Transactional
    public void sendPurchaseOrderForApproval(UUID purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with id " + purchaseOrderId));
        if (purchaseOrder.getStatus() != OrderStatus.DRAFT) {
            throw new PurchaseOrderIllegalStatusException("Only DRAFT status purchase order are send to approval.");
        }
        if (purchaseOrder.getItems() == null || purchaseOrder.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Purchase order does not contains any product for approval.");
        }
        purchaseOrder.setStatus(OrderStatus.PENDING_APPROVAL);
    }

    public List<PurchaseOrderResponse> getAllPurchaseOrder() {
        UUID currentUserId = userService.getCurrentUserId();
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByCreatedBy_Id(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("No purchase order created by " + currentUserId + " found"));
        return purchaseOrders.stream()
                .map(purchaseOrderMapper::toResponse)
                .toList();
    }
}
