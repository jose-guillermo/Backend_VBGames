package com.vbgames.backend.authservice;

import java.util.Set;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.authservice.entities.Role;
import com.vbgames.backend.authservice.entities.User;
import com.vbgames.backend.authservice.repositories.RoleRepository;
import com.vbgames.backend.authservice.repositories.UserRepository;

import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        Role userRole = roleRepository.findByName("ROLE_USER").get();

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setVerified(true);
        admin.setRoles(new ArrayList<>(Set.of(userRole, adminRole)));
        userRepository.save(admin);
    }
}
