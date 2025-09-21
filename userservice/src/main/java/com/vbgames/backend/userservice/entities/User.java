package com.vbgames.backend.userservice.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;
    private int coins;

    @Column(name = "creation_date_epoch")
    private long creationDateEpoch;
    private boolean online;
    
    @ManyToOne
    @JoinColumn(name = "favourite_game")
    private Game favouriteGame;
    
    @ManyToMany(cascade = CascadeType.REMOVE)
    // @JsonIgnoreProperties({"users", "handler", "hibernateLazyInitializer"})
    @JsonIgnoreProperties({"users"})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    public User(){
        this.creationDateEpoch = Instant.now().getEpochSecond();
        this.roles = new ArrayList<>();
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.creationDateEpoch = Instant.now().getEpochSecond();
        this.roles = new ArrayList<>();
    }
}
