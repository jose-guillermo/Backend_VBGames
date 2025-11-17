package com.vbgames.backend.messageservice.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.messageservice.dtos.MessageResponse;
import com.vbgames.backend.messageservice.dtos.SendMessageRequest;
import com.vbgames.backend.messageservice.entities.Message;
import com.vbgames.backend.messageservice.entities.User;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {

    MessageResponse toMessageResponse(Message message);

    List<MessageResponse> toMessageResponses(List<Message> messages);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "read", ignore = true)
    @Mapping(target = "sendDate", ignore = true)
    Message toMessage(SendMessageRequest messageResponse, User sender, User recipient);



}
