package com.vbgames.backend.ratingservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.ratingservice.dtos.UserResponse;
import com.vbgames.backend.ratingservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", ignore = true)
    User toUser(UserCreatedEvent event);

    User toUser(UsernameUpdatedEvent event);

    UserResponse toUserResponse(User user);
}

