package com.vbgames.backend.authservice.entities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    // @JsonIgnoreProperties({"users", "handler", "hibernateLazyInitializer"})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    private boolean verified;

    @Column(name = "expires_at")
    private Long expiresAt;

    public User(){
        this.roles = new ArrayList<>();
        this.expiresAt = Instant.now().plus(20, ChronoUnit.MINUTES).toEpochMilli();
    }

    public User(String password, String email) {
        this.password = password;
        this.email = email;
        this.verified = false;
        this.roles = new ArrayList<>();
        this.expiresAt = Instant.now().plus(20, ChronoUnit.MINUTES).toEpochMilli();
    }

    public void addRefreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = new RefreshToken(refreshToken, this);
        this.refreshTokens.add(refreshTokenEntity);
    }

}
