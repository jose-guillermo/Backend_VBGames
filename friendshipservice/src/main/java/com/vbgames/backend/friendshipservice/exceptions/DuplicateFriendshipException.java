package com.vbgames.backend.friendshipservice.exceptions;

public class DuplicateFriendshipException extends RuntimeException {

    public DuplicateFriendshipException() {
        super();
    }

    public DuplicateFriendshipException(String message) {
        super(message);
    }

}
