package com.vbgames.backend.userservice;

import java.util.Set;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.ApplicationRunner;

import com.vbgames.backend.userservice.entities.Role;
import com.vbgames.backend.userservice.entities.User;
import com.vbgames.backend.userservice.repositories.RoleRepository;
import com.vbgames.backend.userservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${admin.email}")
    private String adminEmail;

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
        admin.setUsername("admin");
        admin.setRoles(new ArrayList<>(Set.of(userRole, adminRole)));
        userRepository.save(admin);
    }
}
