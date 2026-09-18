package com.inventory_management.services;

import com.inventory_management.model.dtos.LoginRequest;
import com.inventory_management.model.dtos.LoginResponse;
import com.inventory_management.model.dtos.RegisterRequest;
import com.inventory_management.model.dtos.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
