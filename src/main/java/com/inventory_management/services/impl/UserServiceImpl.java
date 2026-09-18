package com.inventory_management.services.impl;

import com.inventory_management.exception.ResourceNotFoundException;
import com.inventory_management.model.dtos.UserResponse;
import com.inventory_management.model.entity.Permission;
import com.inventory_management.model.entity.User;
import com.inventory_management.repositories.UserRepository;
import com.inventory_management.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserById(UUID id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toResponse(foundUser);
    }

    public UserResponse getUserByEmail(String email) {
        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toResponse(foundUser);
    }

    public boolean userExistsByEmail(String email) {
        boolean found = userRepository.existsByEmail(email);
        System.out.println(found);
        return found;
    }

    public List<UserResponse> getAllUser() {
        List<User> allUsers = userRepository.findAll();
        return allUsers
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setModifiedAt(user.getModifiedAt());
        userResponse.setFullName(user.getFirstName() + " " + user.getLastName());
        userResponse.setRole(
                user.getRole().getName()
        );
        userResponse.setPermissions(
                user.getRole()
                        .getPermissions()
                        .stream()
                        .map(Permission::getName)
                        .collect(Collectors.toSet())
        );

        return userResponse;
    }

}
