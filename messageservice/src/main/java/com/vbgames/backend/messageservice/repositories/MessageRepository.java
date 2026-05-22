package com.vbgames.backend.messageservice.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.messageservice.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findAllByRecipientId(UUID senderId);
}
