package com.vbgames.backend.realtimeservice.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friendships")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Friendship {

    @EmbeddedId
    private FriendshipId id;

}
