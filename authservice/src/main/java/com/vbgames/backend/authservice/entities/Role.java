package com.vbgames.backend.authservice.entities;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    @Id
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    // @JsonIgnoreProperties({"roles", "handler", "hibernateLazyInitializer"})
    @ToString.Exclude
    private List<User> users;

}
