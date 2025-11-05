package com.vbgames.backend.productservice.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.productservice.entities.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, UUID> {

    @Query("""
        SELECT DISTINCT p 
        FROM Product p
        LEFT JOIN FETCH p.users
        LEFT JOIN FETCH p.game
    """)
    List<Product> findAllWithUsers();

    Optional<Product> findByName(String name);
}
