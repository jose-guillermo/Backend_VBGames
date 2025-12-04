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
    @Mapping(target = "expiredAt", ignore = true)
    User toUser(RegisterRequest userDto);

    UserResponse toUserResponse(User user);

    @Mapping(target = "username", source = "username")
    UserCreatedEvent toUserCreatedEvent(User user, String username);

    default String mapRoleToString(Role role) {
        return role.getName();
    }
}
