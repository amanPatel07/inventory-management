package com.inventory_management.services;

import com.inventory_management.model.dtos.UserResponse;
import com.inventory_management.model.entity.User;
import com.inventory_management.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserResponse getUserById(UUID id);

    UserResponse getUserByEmail(String email);

    boolean userExistsByEmail(String email);

    List<UserResponse> getAllUser();
}
