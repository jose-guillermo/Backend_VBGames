package com.vbgames.backend.authservice.entities;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    @Id
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    // @JsonIgnoreProperties({"roles", "handler", "hibernateLazyInitializer"})
    private List<User> users;

}
