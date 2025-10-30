package com.vbgames.backend.friendshipservice.exceptions;

public class SelfFriendRequestException extends RuntimeException {

    public SelfFriendRequestException() {
        super();
    }
    
    public SelfFriendRequestException(String message) {
        super(message);
    }

}
