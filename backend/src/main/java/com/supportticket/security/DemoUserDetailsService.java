package com.supportticket.security;

import com.supportticket.config.DemoUserProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DemoUserDetailsService implements UserDetailsService {

    private final DemoUserProperties demoUserProperties;
    private final PasswordEncoder passwordEncoder;

    public DemoUserDetailsService(DemoUserProperties demoUserProperties, PasswordEncoder passwordEncoder) {
        this.demoUserProperties = demoUserProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return demoUserProperties.getDemoUsers().stream()
                .filter(demoUser -> demoUser.getUsername().equals(username))
                .findFirst()
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private UserDetails toUserDetails(DemoUserProperties.DemoUser demoUser) {
        return User.builder()
                .username(demoUser.getUsername())
                .password(passwordEncoder.encode(demoUser.getPassword()))
                .roles(demoUser.getRole())
                .build();
    }
}
