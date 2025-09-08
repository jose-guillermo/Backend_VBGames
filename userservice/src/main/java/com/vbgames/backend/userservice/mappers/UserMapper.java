package com.vbgames.backend.userservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.userservice.dtos.UserRequest;
import com.vbgames.backend.userservice.dtos.UserResponse;
import com.vbgames.backend.userservice.entities.Role;
import com.vbgames.backend.userservice.entities.User;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserMapper {
    
    User toUser(UserRequest userDto);

    @Mapping(target = "password", ignore = true)
    UserResponse toUserDto(User user);

    default String mapRoleToString(Role role) {
        return role.getName();
    }

    default Role mapStringToRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }
}
