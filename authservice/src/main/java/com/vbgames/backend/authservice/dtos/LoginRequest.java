package com.vbgames.backend.authservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {

    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Debe ser una dirección de correo electrónico válida"
    )
    @NotNull(message = "El email no puede estar vacio")
    private String email;

    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "La contraseña debe tener al menos una minúscula, una mayúscula, un número y mínimo 8 caracteres"
    )
    @NotNull(message = "La contraseña es obligatoria")
    private String password;
}
