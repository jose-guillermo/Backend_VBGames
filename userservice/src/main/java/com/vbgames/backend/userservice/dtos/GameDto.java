package com.vbgames.backend.userservice.dtos;

import java.util.UUID;

import com.vbgames.backend.common.validators.IsUUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

    @IsUUID
    @NotBlank(message = "El id no puede estar vacio")
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String name;

}
