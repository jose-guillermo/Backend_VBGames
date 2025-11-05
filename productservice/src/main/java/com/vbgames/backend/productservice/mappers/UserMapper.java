package com.vbgames.backend.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.UpdateCoinsEvent;
import com.vbgames.backend.common.events.UserEvent;
import com.vbgames.backend.productservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "products", ignore = true)
    User toUser(UserEvent userRequest);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "username", ignore = true)
    User toUser(UpdateCoinsEvent updateCoinsEvent);

    UpdateCoinsEvent toUpdateCoinsEvent(User updateCoinsEvent);
}
