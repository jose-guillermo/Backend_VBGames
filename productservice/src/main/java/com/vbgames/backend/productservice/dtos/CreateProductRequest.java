package com.vbgames.backend.productservice.dtos;

import com.vbgames.backend.productservice.enums.ProductType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @NotBlank
    private String name;

    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    @NotNull(message = "El precio no puede estar vacio")
    private int price;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private ProductType type;
}
