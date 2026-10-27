package com.inventory_management.controllers;

import com.inventory_management.models.dtos.ApiResponse;
import com.inventory_management.models.dtos.UserAuthorizationResponse;
import com.inventory_management.models.dtos.UserResponse;
import com.inventory_management.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getUserDetails() {
        UserResponse user = userService.getUserDetails();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "success",
                        user
                ));
    }

    @GetMapping("/me/authorization")
    public ResponseEntity<ApiResponse<UserAuthorizationResponse>> getUserAuthorization() {
        UserAuthorizationResponse userAuthorizationResponse = userService.getUserAuthorization();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "success",
                        userAuthorizationResponse
                ));
    }

}
