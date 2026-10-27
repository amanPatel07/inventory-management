package com.inventory_management.exceptions;

public class PurchaseOrderIllegalStatusException extends RuntimeException {
    public PurchaseOrderIllegalStatusException(String message) {
        super(message);
    }
}
