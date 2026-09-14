package com.inventory_management.model.dtos;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Getter
@Setter
public class ProductRequest {

    private String name;
    private String category;
    private double price;
    private int minimumStock;

    @NotNull
    private UUID supplierId;
}
