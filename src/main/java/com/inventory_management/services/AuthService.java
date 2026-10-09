package com.inventory_management.services;

import com.inventory_management.models.dtos.LoginRequest;
import com.inventory_management.models.dtos.LoginResponse;
import com.inventory_management.models.dtos.RegisterRequest;
import com.inventory_management.models.dtos.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
