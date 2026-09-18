package com.inventory_management.services.impl;

import com.inventory_management.exception.ResourceNotFoundException;
import com.inventory_management.exception.UserAlreadyExistsException;
import com.inventory_management.model.dtos.LoginRequest;
import com.inventory_management.model.dtos.LoginResponse;
import com.inventory_management.model.dtos.RegisterRequest;
import com.inventory_management.model.dtos.UserResponse;
import com.inventory_management.model.entity.Permission;
import com.inventory_management.model.entity.Role;
import com.inventory_management.model.entity.User;
import com.inventory_management.repositories.RoleRepository;
import com.inventory_management.repositories.UserRepository;
import com.inventory_management.services.AuthService;
import com.inventory_management.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {

        if (request == null) {
            throw new ResourceNotFoundException("Something went wrong");
        }

        // Check if user already exists with email
        boolean userExists = userRepository.existsByEmail(request.getEmail());
        if (userExists) {
            throw new UserAlreadyExistsException("User exists with email.");
        }

        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        // Set the default role as "WAREHOUSE_USER"
        Role role = roleRepository.findByName("WAREHOUSE_USER")
                        .orElseThrow(() -> new ResourceNotFoundException("Something went wrong"));
        newUser.setRole(role);
        newUser.setCreatedAt(new Date());
        newUser.setModifiedAt(new Date());
        User savedUser = userRepository.save(newUser);
        return toResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
        );

        User user = userRepository
                .findByEmail(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateJwtToken(authentication);

        Set<String> permissions = user.getRole()
                .getPermissions()
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return new LoginResponse(
                token,
                user.getRole().getName(),
                permissions
        );
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
