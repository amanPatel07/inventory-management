package com.inventory_management.services;

import com.inventory_management.models.dtos.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse getUserById(UUID id);

    UserResponse getUserByEmail(String email);

    boolean userExistsByEmail(String email);

    List<UserResponse> getAllUser();
}
