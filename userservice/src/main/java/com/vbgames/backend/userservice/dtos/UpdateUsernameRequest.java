package com.vbgames.backend.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUsernameRequest {

    @NotBlank(message = "El username no puede estar vacio")
    private String username;

}
