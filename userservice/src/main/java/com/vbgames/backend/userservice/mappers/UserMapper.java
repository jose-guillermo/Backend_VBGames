package com.vbgames.backend.userservice.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UpdateCoinsEvent;
import com.vbgames.backend.common.events.UserEvent;
import com.vbgames.backend.userservice.dtos.RegisterRequest;
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
    User toUser(RegisterRequest userDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "coins", source = "coins")
    User toUser(UpdateCoinsEvent updateCoinsEvent);

    UserResponse toUserResponse(User user);

    UserEvent toUserEvent(User user);

    default String mapRoleToString(Role role) {
        return role.getName();
    }
}
