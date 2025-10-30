package com.vbgames.backend.friendshipservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UserEvent;
import com.vbgames.backend.friendshipservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "friendOf", ignore = true)
    User toUser(UserEvent userEvent);
}
