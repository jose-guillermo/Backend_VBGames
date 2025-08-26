package com.vbgames.backend.gameservice.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vbgames.backend.gameservice.dtos.GameDto;
import com.vbgames.backend.gameservice.entities.Game;

@Mapper(componentModel = "spring", uses = {PieceMapper.class})
public interface GameMapper {

    GameDto toGameDto(Game game);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pieces", ignore = true)
    Game toGame(GameDto gameDto);   

    List<GameDto> toGamesDto(List<Game> games);

}
