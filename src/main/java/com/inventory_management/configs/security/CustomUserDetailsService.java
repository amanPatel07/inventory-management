package com.inventory_management.configs.security;

import com.inventory_management.exceptions.ResourceNotFoundException;
import com.inventory_management.models.entity.Permission;
import com.inventory_management.models.entity.Role;
import com.inventory_management.models.entity.User;
import com.inventory_management.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email" + email));
        Set<GrantedAuthority> authorities = new HashSet<>();
        // Add role
        for (Role role : user.getRoles()) {
            authorities.add(
                    new SimpleGrantedAuthority("ROLE_" + role.getName())
            );
            for (Permission permission : role.getPermissions()) {
                authorities.add(
                        new SimpleGrantedAuthority(permission.getName())
                );
            }
            logger.info("Current user role: {}, permissions: {}",
                    role.getName(),
                    role.getPermissions().stream()
                            .map(Permission::getName)
                            .toList());
            logger.info("Current user authorities: {}",
                    authorities.stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList()
            );
        }
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
