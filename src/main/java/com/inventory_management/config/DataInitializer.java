package com.inventory_management.config;

import com.inventory_management.model.entity.Permission;
import com.inventory_management.model.entity.Role;
import com.inventory_management.repositories.PermissionRepository;
import com.inventory_management.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public DataInitializer(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public void run(String ...args) {

        //Permission
        Permission productRead = getOrCreatePermission("PRODUCT_READ");
        Permission productCreate = getOrCreatePermission("PRODUCT_CREATE");
        Permission productUpdate = getOrCreatePermission("PRODUCT_UPDATE");
        Permission stockRead = getOrCreatePermission("STOCK_READ");
        Permission stockCreate = getOrCreatePermission("STOCK_CREATE");
        Permission stockUpdate = getOrCreatePermission("STOCK_UPDATE");
        Permission reportRead = getOrCreatePermission("REPORT_READ");

        //Role
        Role admin = getOrCreateRole("ADMIN");
        Role warehouseUser = getOrCreateRole("WAREHOUSE_USER");

        admin.setPermissions(new HashSet<>(Set.of(
                productRead,
                productCreate,
                productUpdate,
                stockRead,
                stockCreate,
                stockUpdate
        )));

        warehouseUser.setPermissions(new HashSet<>(Set.of(
                productRead,
                stockRead,
                stockCreate,
                reportRead
        )));

        // Save roles
        roleRepository.save(admin);
        roleRepository.save(warehouseUser);
    }

    private Permission getOrCreatePermission(String permissionName) {
        return permissionRepository
                .findByName(permissionName)
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName(permissionName);
                    return permissionRepository.save(permission);
                });
    }

    private Role getOrCreateRole(String roleName) {
        return roleRepository
                .findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }
}
