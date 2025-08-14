package com.vdgames.backend.gameservice.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vdgames.backend.gameservice.dto.GameDto;
import com.vdgames.backend.gameservice.entities.Game;

@Mapper(componentModel = "spring", uses = {PieceMapper.class})
public interface GameMapper {

    GameDto toGameDto(Game game);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pieces", source = "pieces")
    Game toGame(GameDto gameDto);   

    List<GameDto> toGamesDto(List<Game> games);
}
