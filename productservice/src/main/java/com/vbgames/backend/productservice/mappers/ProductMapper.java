package com.vbgames.backend.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.common.events.ProductPurchasedEvent;
import com.vbgames.backend.productservice.dtos.CreateProductRequest;
import com.vbgames.backend.productservice.dtos.ProductResponse;
import com.vbgames.backend.productservice.entities.Product;
import com.vbgames.backend.productservice.entities.User;

@Mapper(componentModel = "spring", uses = GameMapper.class)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "users", ignore = true)
    Product toProduct(CreateProductRequest productDto);

    @Mapping(target = "owned", source = "owned")
    ProductResponse toProductResponse(Product product, boolean owned);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "userId", source = "user.id")
    ProductPurchasedEvent toProductPurchasedEvent(Product product, User user);
}
