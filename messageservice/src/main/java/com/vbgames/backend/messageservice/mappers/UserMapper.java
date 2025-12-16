package com.vbgames.backend.messageservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.messageservice.dtos.UserResponse;
import com.vbgames.backend.messageservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", ignore = true)
    User toUser(UserCreatedEvent userEvent);

    User toUser(UsernameUpdatedEvent userEvent);

    UserResponse toUserResponse(User user);

}
