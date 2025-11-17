package com.vbgames.backend.friendshipservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.friendshipservice.dtos.FriendResponse;
import com.vbgames.backend.friendshipservice.services.FriendshipService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<FriendResponse> getFriends(@RequestHeader("X-User-Id") UUID userId) {
        return friendshipService.getFriends(userId);
    }

    @PostMapping("/{friendId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFriendship(@RequestHeader("X-User-Id") UUID userId, @PathVariable @IsUUID String friendId) {
        friendshipService.sendFrienshipRequest(userId, UUID.fromString(friendId));
    }

    @DeleteMapping("/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriendship(@RequestHeader("X-User-Id") UUID userId, @PathVariable @IsUUID String friendId) {
        friendshipService.removeFriendship(userId, UUID.fromString(friendId));
    }

    @PatchMapping("/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptFriendship(@RequestHeader("X-User-Id") UUID userId, @PathVariable @IsUUID String friendId) {
        friendshipService.acceptFriendship(userId, UUID.fromString(friendId));
    }
}
