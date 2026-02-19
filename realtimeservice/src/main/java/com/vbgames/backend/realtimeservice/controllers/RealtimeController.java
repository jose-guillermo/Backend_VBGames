package com.vbgames.backend.realtimeservice.controllers;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RealtimeController {

    @MessageMapping("/mensaje")
    @SendTo("/topic/chat")
    public String getMessage(String message) {
        System.out.println("Servidor recibió: " + message);
        return "Servidor recibió: " + message; 
    }

    @MessageMapping("/friendConected")
    @SendTo("/topic/friendConected")
    public UUID friendConected(UUID userId) {
        return userId;
    }
    
}
