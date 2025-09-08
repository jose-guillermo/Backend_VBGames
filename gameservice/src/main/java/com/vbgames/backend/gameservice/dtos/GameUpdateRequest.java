package com.vbgames.backend.gameservice.dtos;

import java.util.List;

import com.vbgames.backend.common.validators.IsUUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameUpdateRequest {

    @IsUUID
    private String id;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String name;

    @NotEmpty(message = "Las piezas no pueden estar vacias")
    private List<PieceDto> pieces;
}
