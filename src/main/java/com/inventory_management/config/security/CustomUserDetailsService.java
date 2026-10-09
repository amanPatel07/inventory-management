package com.inventory_management.config.security;

import com.inventory_management.exception.ResourceNotFoundException;
import com.inventory_management.model.entity.User;
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
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email" + email));
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add role
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
        user.getRole()
                .getPermissions()
                .forEach(permission -> authorities.add(
                        new SimpleGrantedAuthority(
                                permission.getName()
                        )
                ));

        logger.info("Current user role: {}, permissions: {}",
                user.getRole().getName(),
                user.getRole().getPermissions().stream()
                        .map(permission -> permission.getName())
                        .toList());
        logger.info("Current user authorities: {}",
                authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
