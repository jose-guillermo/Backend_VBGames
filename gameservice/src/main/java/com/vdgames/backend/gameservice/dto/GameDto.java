package com.vdgames.backend.gameservice.dto;

import java.util.List;

import com.vdgames.backend.gameservice.validators.ExistsByName;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {

    private String id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @ExistsByName
    private String name;

    @NotBlank(message = "Las piezas no pueden estar vacias")
    private List<PieceDto> pieces;
}
