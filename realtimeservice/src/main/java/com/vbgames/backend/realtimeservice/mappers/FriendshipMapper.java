package com.vbgames.backend.realtimeservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.vbgames.backend.common.events.FriendshipEvent;
import com.vbgames.backend.realtimeservice.entities.Friendship;
import com.vbgames.backend.realtimeservice.entities.FriendshipId;

@Mapper(componentModel = "spring")
public interface FriendshipMapper{

    @Mapping(target = "id", source = ".", qualifiedByName = "toFriendshipId")
    Friendship toFriendship(FriendshipEvent event);

    @Named("toFriendshipId")
    default FriendshipId toFriendshipId(FriendshipEvent event) {
        return new FriendshipId(event.getSenderId(), event.getRecipientId());
    }
}
