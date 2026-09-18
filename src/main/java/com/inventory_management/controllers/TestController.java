package com.inventory_management.controllers;

import com.inventory_management.model.dtos.UserResponse;
import com.inventory_management.services.AuthService;
import com.inventory_management.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public boolean findUserByEmail(@RequestBody String email) {
        return userService.userExistsByEmail(email);

    }

    @GetMapping
    public List<UserResponse> getAllUser() {
        return userService.getAllUser();
    }

}
