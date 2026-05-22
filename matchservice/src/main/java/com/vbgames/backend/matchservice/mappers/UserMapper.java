package com.vbgames.backend.matchservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.matchservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "favouriteMatches", ignore = true)
    User toUser(UserCreatedEvent event);

    @Mapping(target = "favouriteMatches", ignore = true)
    User toUser(UsernameUpdatedEvent event);
}