package com.inventory_management.model.dtos;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Getter
@Setter
public class ProductRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @DecimalMin(value = "0.0", inclusive = true)
    private double price;
    @Min(0)
    private int minimumStock;

    @NotNull
    private UUID supplierId;
}
