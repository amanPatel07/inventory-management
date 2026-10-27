package com.inventory_management.models.dtos;

import java.util.Set;

public record UserAuthorizationResponse(
        Set<String> roles,
        Set<String> permissions
) {}
