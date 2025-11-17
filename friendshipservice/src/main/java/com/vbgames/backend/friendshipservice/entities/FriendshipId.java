package com.vbgames.backend.friendshipservice.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipId implements Serializable {

    @Column(name = "user_id_1")
    private UUID userId1;
    @Column(name = "user_id_2")
    private UUID userId2;

}