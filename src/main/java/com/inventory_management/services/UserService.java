package com.inventory_management.services;

import com.inventory_management.models.dtos.UserAuthorizationResponse;
import com.inventory_management.models.dtos.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse getUserById(UUID id);

    private UserResponse getUserByEmail(String email) {
        return null;
    }

    boolean userExistsByEmail(String email);

    List<UserResponse> getAllUser();

    public UserResponse getUserDetails();

    UserAuthorizationResponse getUserAuthorization();

    UUID getCurrentUserId();
}
