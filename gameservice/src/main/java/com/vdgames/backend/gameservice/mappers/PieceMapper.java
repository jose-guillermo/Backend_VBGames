package com.vdgames.backend.gameservice.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.vdgames.backend.gameservice.dto.PieceDto;
import com.vdgames.backend.gameservice.entities.Game;
import com.vdgames.backend.gameservice.entities.Piece;
import com.vdgames.backend.gameservice.entities.PieceId;

@Mapper(componentModel = "spring")
public interface PieceMapper {

    @Mapping(target = "id", source = ".", qualifiedByName = "toPieceId")
    Piece toPiece(PieceDto pieceDto, @Context Game game);

    @Mapping(target = "name", source = "id.name")
    @Mapping(target = "color", source = "id.color")
    PieceDto toPieceDto(Piece piece);

    @Named("toPieceId")
    default PieceId toPieceId(PieceDto dto, @Context Game game) {
        PieceId id = new PieceId();
        id.setName(dto.getName());
        id.setColor(dto.getColor());
        id.setGameId(game.getId());
        return id;
    }
}
