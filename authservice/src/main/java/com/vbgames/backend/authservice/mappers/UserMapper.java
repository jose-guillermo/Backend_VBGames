package com.vbgames.backend.authservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.authservice.dtos.RegisterRequest;
import com.vbgames.backend.authservice.dtos.UserResponse;
import com.vbgames.backend.authservice.entities.Role;
import com.vbgames.backend.authservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "verified", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    User toUser(RegisterRequest userDto);

    UserResponse toUserResponse(User user);

    UserCreatedEvent toUserCreatedEvent(User user);

    default String mapRoleToString(Role role) {
        return role.getName();
    }
}
