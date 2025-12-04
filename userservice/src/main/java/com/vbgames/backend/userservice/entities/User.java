package com.vbgames.backend.userservice.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;
    
    private String username;

    private String email;
    private int coins;

    @Column(name = "created_at")
    private long creationDateEpoch;
    private boolean online;
    
    @ManyToOne
    @JoinColumn(name = "favourite_game")
    private Game favouriteGame;
    
    @ManyToMany
    // @JsonIgnoreProperties({"users", "handler", "hibernateLazyInitializer"})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    public User(){
        this.creationDateEpoch = Instant.now().toEpochMilli();
        this.roles = new ArrayList<>();
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.creationDateEpoch = Instant.now().toEpochMilli();
        this.roles = new ArrayList<>();
    }
}
