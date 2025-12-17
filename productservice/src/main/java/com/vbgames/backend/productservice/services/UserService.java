package com.vbgames.backend.productservice.services;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.UserCoinsUpdatedEvent;
import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.productservice.entities.Product;
import com.vbgames.backend.productservice.entities.User;
import com.vbgames.backend.productservice.exceptions.InsufficientCoinsException;
import com.vbgames.backend.productservice.exceptions.ProductAlreadyOwnedException;
import com.vbgames.backend.productservice.mappers.UserMapper;
import com.vbgames.backend.productservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, UserCoinsUpdatedEvent> kafkaTemplate;

    @KafkaListener(topics = "user.created")
    @Transactional
    public void handleUserEvent(UserCreatedEvent event) {
        User user = userMapper.toUser(event);
        userRepository.save(user);
    }

    @KafkaListener(topics = "username.updated")
    @Transactional
    public void handleUserEvent(UsernameUpdatedEvent event) {
        User user = userRepository.findById(event.getId()).get();
        user.setUsername(event.getUsername());
    }

    @Transactional
    public User buyProduct(UUID userId, Product product) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
        
        if (user.getProducts().contains(product))
            throw new ProductAlreadyOwnedException("Ya tienes este producto"); 

        if (user.getCoins() < product.getPrice())
            throw new InsufficientCoinsException("No tienes suficientes monedas para comprar este producto");
   
        user.setCoins(user.getCoins() - product.getPrice());

        user.getProducts().add(product);

        sendUpdateCoinsEvent(user);

        return user;
    }

    private void sendUpdateCoinsEvent(User user) {
        UserCoinsUpdatedEvent updateCoinsEvent = userMapper.toUpdateCoinsEvent(user);
        kafkaTemplate.send("user.coins.updated", updateCoinsEvent);
    }
}
