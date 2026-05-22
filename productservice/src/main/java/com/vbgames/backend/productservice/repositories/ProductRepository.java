package com.vbgames.backend.productservice.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.productservice.dtos.ProductResponse;
import com.vbgames.backend.productservice.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Consulta que devuelve todos los productos de un usuario y si le pertenece o no
    @Query("""
        SELECT new com.vbgames.backend.productservice.dtos.ProductResponse(
            p.id, 
            p.name, 
            p.price, 
            p.type, 
            new com.vbgames.backend.productservice.dtos.GameResponse(g.id, g.name),
            CASE WHEN u.id IS NOT NULL THEN true ELSE false END
        )
        FROM Product p
        LEFT JOIN p.game g
        LEFT JOIN p.users u WITH u.id = :userId
        """
    )
    List<ProductResponse> findAllProductsByUserId(UUID userId);

    Optional<Product> findByName(String name);
}