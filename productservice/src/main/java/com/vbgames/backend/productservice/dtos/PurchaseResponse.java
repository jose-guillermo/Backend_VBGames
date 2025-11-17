package com.vbgames.backend.productservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private ProductResponse product;
    private int userCoins;
}
