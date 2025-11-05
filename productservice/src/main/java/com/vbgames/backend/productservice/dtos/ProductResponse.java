package com.vbgames.backend.productservice.dtos;

import java.util.UUID;

import com.vbgames.backend.productservice.enums.ProductType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private UUID id;
    private String name;
    private int price;
    private ProductType type;
    private GameResponse game;
    private boolean owned;

}
