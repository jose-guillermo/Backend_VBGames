package com.vbgames.backend.friendshipservice.exceptions;

import com.vbgames.backend.common.enums.ErrorCode;

import lombok.Getter;

@Getter
public class SelfFriendRequestException extends RuntimeException {

    private ErrorCode errorCode;
    
    public SelfFriendRequestException(String message) {
        super(message);
        this.errorCode = ErrorCode.SELF_FRIEND_REQUEST;
    }

}
