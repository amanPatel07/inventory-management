package com.inventory_management.controllers;

import com.inventory_management.model.dtos.UserResponse;
import com.inventory_management.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public boolean findUserByEmail(@RequestBody String email) {
        return userService.userExistsByEmail(email);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponse> getAllUser() {
        return userService.getAllUser();
    }

}
