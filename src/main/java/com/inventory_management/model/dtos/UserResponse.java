package com.inventory_management.model.dtos;

import com.inventory_management.model.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String role;
    private Set<String> permissions;
    private Date createdAt;
    private Date modifiedAt;

}
