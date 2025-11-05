package com.vbgames.backend.userservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUsernameRequest {

    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    @NotNull(message = "El username es obligatorio")
    private String username;

}
