package com.vbgames.backend.gameservice.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

    private UUID id;

    private String name;

    private List<PieceDto> pieces;
}
