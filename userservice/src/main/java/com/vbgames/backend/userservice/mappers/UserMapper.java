package com.vbgames.backend.userservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.entities.Role;
import com.vbgames.backend.userservice.entities.User;

@Mapper(componentModel = "spring", uses = GameMapper.class)
public interface UserMapper {

    @Mapping(target = "coins", ignore = true)
    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "online", ignore = true)
    @Mapping(target = "favouriteGame", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "creationDateEpoch", ignore = true)
    User toUser(UserCreatedEvent user);

    UserResponse toUserResponse(User user);

    UsernameUpdatedEvent toUsernameUpdatedEvent(User user);

    default String mapRoleToString(Role role) {
        return role.getName();
    }
}
