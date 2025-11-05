package com.vbgames.backend.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.productservice.dtos.CreateProductRequest;
import com.vbgames.backend.productservice.dtos.ProductResponse;
import com.vbgames.backend.productservice.entities.Product;

@Mapper(componentModel = "spring", uses = GameMapper.class)
public interface ProductMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "users", ignore = true)
    Product toProduct(CreateProductRequest productDto);
    
    @Mapping(target = "owned", source = "owned")
    ProductResponse toProductResponse(Product product, boolean owned);
}
