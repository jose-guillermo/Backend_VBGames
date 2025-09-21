package com.vbgames.backend.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El username no puede estar vacio")
    public String email;

    @NotBlank(message = "La contraseña es necesaria")
    public String password;

}
