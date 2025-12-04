package com.vbgames.backend.messageservice.mappers;

import org.mapstruct.Mapper;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.messageservice.dtos.UserResponse;
import com.vbgames.backend.messageservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreatedEvent userEvent);

    User toUser(UsernameUpdatedEvent userEvent);

    UserResponse toUserResponse(User user);

}
