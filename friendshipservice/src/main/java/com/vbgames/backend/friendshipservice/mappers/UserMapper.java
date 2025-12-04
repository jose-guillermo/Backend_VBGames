package com.vbgames.backend.friendshipservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.friendshipservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "friendOf", ignore = true)
    User toUser(UserCreatedEvent userEvent);

    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "friendOf", ignore = true)
    User toUser(UsernameUpdatedEvent userEvent);
}
