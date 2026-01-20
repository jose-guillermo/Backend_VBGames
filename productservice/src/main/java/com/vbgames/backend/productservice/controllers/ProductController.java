package com.vbgames.backend.productservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vbgames.backend.common.exceptions.RequestValidationException;
import com.vbgames.backend.common.validators.IsUUID;
import com.vbgames.backend.productservice.dtos.ProductResponse;
import com.vbgames.backend.productservice.dtos.PurchaseResponse;
import com.vbgames.backend.productservice.dtos.CreateProductRequest;
import com.vbgames.backend.productservice.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
        summary = "Obtener productos"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProducts(@RequestHeader("X-User-Id") UUID userId) {
        return productService.getProducts(userId);
    }

    @Operation(
        summary = "Crear producto",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR\n" +
            "- 404 → GAME_NOT_FOUND" +
            "- 409 → PRODUCT_ALREADY_EXISTS"
    )
    @PostMapping("/{gameIdString}")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
        @PathVariable @IsUUID String gameIdString,
        @RequestBody CreateProductRequest productRequest, 
        BindingResult result
    ) {
        validation(result);
        
        UUID gameId = UUID.fromString(gameIdString);

        return productService.createProduct(productRequest, gameId);
    }

    @Operation(
        summary = "Comprar producto por id",
        description = "Errores posibles:\n" +
            "- 400 → VALIDATION_ERROR"
    )
    @PostMapping("/{productIdString}/purchase")
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseResponse purchaseProduct(
        @RequestHeader("X-User-Id") UUID userId,
        @PathVariable @IsUUID String productIdString
    ) {
        UUID productId = UUID.fromString(productIdString);
        
        return productService.purchaseProduct(userId, productId);
    }
    
    private void validation(BindingResult result) {
        if (!result.hasFieldErrors()) return;
        
        Map<String, String> errors = new HashMap<>();
        
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        
        throw new RequestValidationException(errors);
    }

}
