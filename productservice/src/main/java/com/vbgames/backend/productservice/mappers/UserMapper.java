package com.vbgames.backend.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UserCoinsUpdatedEvent;
import com.vbgames.backend.common.events.UserCreatedEvent;
import com.vbgames.backend.common.events.UsernameUpdatedEvent;
import com.vbgames.backend.productservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "coins", ignore = true)
    @Mapping(target = "username", ignore = true)
    User toUser(UserCreatedEvent event);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "coins", ignore = true)
    User toUser(UsernameUpdatedEvent event);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "username", ignore = true)
    User toUser(UserCoinsUpdatedEvent updateCoinsEvent);

    UserCoinsUpdatedEvent toUpdateCoinsEvent(User updateCoinsEvent);
}
