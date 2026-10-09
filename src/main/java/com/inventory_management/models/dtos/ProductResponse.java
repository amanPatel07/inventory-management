package com.inventory_management.models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductResponse {

    private UUID id;
    private String name;
    private String category;
    private double price;
    private int currentStock;
    private int minimumStock;
    private UUID supplierId;

}
