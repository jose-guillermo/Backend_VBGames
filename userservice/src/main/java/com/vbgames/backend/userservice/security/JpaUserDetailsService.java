package com.vbgames.backend.userservice.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.userservice.entities.User;
import com.vbgames.backend.userservice.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = repository.findByEmail(email);

        if(userOptional.isEmpty())
            throw new UsernameNotFoundException(String.format("El email %s no existe en el sistema!", email));
        
        User user = userOptional.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role-> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
            user.getId().toString(), 
            user.getPassword(),
            // user.isEnabled(),
            true,
            true,
            true,
            true,
            authorities
        );        
    } 
}