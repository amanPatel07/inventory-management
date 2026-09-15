package com.inventory_management.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SupplierResponse {

    private UUID id;
    private String name;
    private String email;
    private String phone;
}
