package com.vbgames.backend.gameservice.mappers;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.vbgames.backend.gameservice.dtos.GameCreateRequest;
import com.vbgames.backend.gameservice.dtos.GameUpdateRequest;
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

    default void updatePieces(Game game, GameUpdateRequest gameDto) {
        List<Piece> newPieces = toPieces(gameDto.getPieces(), game);

        // Eliminamos las piezas de game que ya existen en newPieces
        game.getPieces().removeIf(piece -> !newPieces.contains(piece));

        // Eliminamos las piezas que ya existen en game
        newPieces.removeIf(piece -> game.getPieces().contains(piece));

        // Añadimos las nuevas piezas
        game.getPieces().addAll(newPieces);
    }

    default void addPieces(Game game, GameCreateRequest gameDto) {
        List<Piece> newPieces = toPieces(gameDto.getPieces(), game);

        // Si el game no tiene pieces, añadira las nuevas
        if(game.getPieces() == null) {
            game.setPieces(newPieces);
            return;
        }
    }

    @Named("toPieceId")
    default PieceId toPieceId(PieceDto pieceDto, @Context Game game) {
        PieceId id = new PieceId();
        id.setName(pieceDto.getName());
        id.setColor(pieceDto.getColor());
        id.setGameId(game.getId());
        return id;
    }

}
