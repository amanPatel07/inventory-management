package com.inventory_management.services.impl;

import com.inventory_management.configs.security.CustomUserDetails;
import com.inventory_management.exceptions.ResourceNotFoundException;
import com.inventory_management.models.dtos.UserAuthorizationResponse;
import com.inventory_management.models.dtos.UserResponse;
import com.inventory_management.models.entity.Permission;
import com.inventory_management.models.entity.Role;
import com.inventory_management.models.entity.User;
import com.inventory_management.repositories.UserRepository;
import com.inventory_management.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserResponse getUserById(UUID id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toResponse(foundUser);
    }

    private UserResponse getUserByEmail(String email) {
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

    public UserResponse getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String email = authentication.getName();
        return getUserByEmail(email);
    }

    @Transactional(readOnly = true)
    public UserAuthorizationResponse getUserAuthorization() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getName());

            for (Permission permission : role.getPermissions()) {
                permissions.add(permission.getName());
            }
        }
        return new UserAuthorizationResponse(roles, permissions);
    }

    public UUID getCurrentUserId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        CustomUserDetails principal =
                (CustomUserDetails) authentication.getPrincipal();

        assert principal != null;
        return principal.getId();
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
        return userResponse;
    }

}
