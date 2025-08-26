package com.vbgames.backend.gameservice.mappers;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.vbgames.backend.gameservice.dtos.PieceDto;
import com.vbgames.backend.gameservice.entities.Game;
import com.vbgames.backend.gameservice.entities.Piece;
import com.vbgames.backend.gameservice.entities.PieceId;

@Mapper(componentModel = "spring")
public interface PieceMapper {

    @Mapping(target = "id", source = ".", qualifiedByName = "toPieceId")
    @Mapping(target = "game", expression = "java(game)")
    Piece toPiece(PieceDto pieceDto, @Context Game game);

    @Mapping(target = "name", source = "id.name")
    @Mapping(target = "color", source = "id.color")
    PieceDto toPieceDto(Piece piece);

    List<Piece> toPieces(List<PieceDto> piecesDto, @Context Game game);

    @Named("toPieceId")
    default PieceId toPieceId(PieceDto dto, @Context Game game) {
        PieceId id = new PieceId();
        id.setName(dto.getName());
        id.setColor(dto.getColor());
        id.setGameId(game.getId());
        return id;
    }

}
