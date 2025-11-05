package com.vbgames.backend.productservice.entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.vbgames.backend.productservice.enums.ProductType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    private String name;
    private int price;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @ToString.Exclude
    private Game game;

    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<User> users;
}
