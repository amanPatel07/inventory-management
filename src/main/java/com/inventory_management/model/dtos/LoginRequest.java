package com.inventory_management.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 9)
    private String password;
}
