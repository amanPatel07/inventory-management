package com.inventory_management.services.impl;

import com.inventory_management.exceptions.ResourceNotFoundException;
import com.inventory_management.exceptions.UserAlreadyExistsException;
import com.inventory_management.models.dtos.LoginRequest;
import com.inventory_management.models.dtos.LoginResponse;
import com.inventory_management.models.dtos.RegisterRequest;
import com.inventory_management.models.dtos.UserResponse;
import com.inventory_management.models.entity.Role;
import com.inventory_management.models.entity.User;
import com.inventory_management.repositories.RoleRepository;
import com.inventory_management.repositories.UserRepository;
import com.inventory_management.services.AuthService;
import com.inventory_management.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
        newUser.getRoles().add(role);
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

        userRepository
                .findByEmail(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateJwtToken(authentication);

        return new LoginResponse(token);
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
