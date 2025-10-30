package com.vbgames.backend.friendshipservice.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    private UUID id;

    private String username;

    @ManyToMany
    @JsonIgnoreProperties({"friendsOf", "handler", "hibernateLazyInitializer"})
    @JoinTable(
        name = "friendships",
        joinColumns = @JoinColumn(name = "user_id_1"),
        inverseJoinColumns = @JoinColumn(name = "user_id_2")
    )
    private List<User> friends = new ArrayList<>();

    @ManyToMany(mappedBy = "friends")
    private List<User> friendOf = new ArrayList<>();

}
