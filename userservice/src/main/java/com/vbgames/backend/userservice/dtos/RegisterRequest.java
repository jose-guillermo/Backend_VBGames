package com.vbgames.backend.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "El username no puede estar vacio")
    private String username;

    @NotBlank(message = "El email no puede estar vacio")
    private String email;

    @NotBlank(message = "El email no puede estar vacio")
    private String password;
}
