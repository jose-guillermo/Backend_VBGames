package com.vbgames.backend.friendshipservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friendships")
@Data
@NoArgsConstructor
public class Friendship {

    @EmbeddedId
    private FriendshipId id;

    @ManyToOne
    @MapsId("userId1")
    @JoinColumn(name = "user_id_1")
    private User user;

    @ManyToOne
    @MapsId("userId2")
    @JoinColumn(name = "user_id_2")
    private User friend;
    
    private boolean accepted;

    @Column(name = "created_at")
    private long createdAt;

    public Friendship(User user, User friend) {
        this.user = user;
        this.friend = friend;
        this.id = new FriendshipId(user.getId(), friend.getId());
        this.accepted = false;
        this.createdAt = System.currentTimeMillis();
    }
}
