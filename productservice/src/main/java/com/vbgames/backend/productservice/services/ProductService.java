package com.vbgames.backend.productservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vbgames.backend.common.enums.ErrorCode;
import com.vbgames.backend.common.events.ProductPurchasedEvent;
import com.vbgames.backend.common.exceptions.DuplicateResourceException;
import com.vbgames.backend.common.exceptions.ResourceNotFoundException;
import com.vbgames.backend.productservice.dtos.ProductResponse;
import com.vbgames.backend.productservice.dtos.PurchaseResponse;
import com.vbgames.backend.productservice.dtos.CreateProductRequest;
import com.vbgames.backend.productservice.entities.Game;
import com.vbgames.backend.productservice.entities.Product;
import com.vbgames.backend.productservice.entities.User;
import com.vbgames.backend.productservice.mappers.ProductMapper;
import com.vbgames.backend.productservice.repositories.GameRepository;
import com.vbgames.backend.productservice.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final GameRepository gameRepository;
    private final UserService userService;
    private final KafkaTemplate<String, ProductPurchasedEvent> kafkaTemplate;

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(UUID userId) {
        List<Product> products = (List<Product>) productRepository.findAllWithUsers();
        
        // Compruebo si el producto pertenece al usuario
        return products.stream()
            .map( product -> {
                boolean owned = product.getUsers().stream()
                    .anyMatch(user -> user.getId().equals(userId));
                return productMapper.toProductResponse(product, owned);
            })
            .toList();
    }
    
    @Transactional
    public ProductResponse createProduct(CreateProductRequest productDto, UUID gameId) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new ResourceNotFoundException("El juego no existe", ErrorCode.GAME_NOT_FOUND));
        
        if(productRepository.findByName(productDto.getName()).isPresent())
            throw new DuplicateResourceException("Ya existe un producto con ese nombre", ErrorCode.PRODUCT_ALREADY_EXISTS);
        
        Product product = productMapper.toProduct(productDto);

        product.setGame(game);

        product = productRepository.save(product);

        return productMapper.toProductResponse(product, false);
    }

    @Transactional
    public PurchaseResponse purchaseProduct(UUID userId, UUID productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado", ErrorCode.PRODUCT_NOT_FOUND));
  
        User user = userService.buyProduct(userId, product);

        sendProductPurchased(product, user);

        return new PurchaseResponse(productMapper.toProductResponse(product, true), user.getCoins());
    }

     private void sendProductPurchased(Product product, User user) {
        ProductPurchasedEvent event = productMapper.toProductPurchasedEvent(product, user);

        kafkaTemplate.send("product.pruchased", event);
    }
}