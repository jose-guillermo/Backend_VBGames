package com.vbgames.backend.messageservice.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.messageservice.entities.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, UUID> {

    List<Message> findAllByRecipientId(UUID senderId);
}
