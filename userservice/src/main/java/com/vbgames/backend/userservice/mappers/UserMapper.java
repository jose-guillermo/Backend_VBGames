package com.vbgames.backend.userservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.userservice.dtos.LoginResponse;
import com.vbgames.backend.userservice.dtos.UserRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.entities.Role;
import com.vbgames.backend.userservice.entities.User;

@Mapper(componentModel = "spring", uses = GameMapper.class)
public interface UserMapper {
    
    @Mapping(target = "coins", ignore = true)
    @Mapping(target = "id", ignore = true) // no necesitas mapear UUID
    @Mapping(target = "online", ignore = true)
    @Mapping(target = "favouriteGame", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "creationDateEpoch", ignore = true)
    User toUser(UserRequest userDto);

    @Mapping(target = "gameDto", ignore = true)
    UserResponse toUserResponse(User user);

    LoginResponse toLoginResponse(User user);

    default String mapRoleToString(Role role) {
        return role.getName();
    }

    default void addRole(User user, Role role) {
        user.getRoles().add(role);
    }
}
