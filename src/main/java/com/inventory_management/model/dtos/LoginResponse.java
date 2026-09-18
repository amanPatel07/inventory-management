package com.inventory_management.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String role;
    private Set<String> permissions;
}
