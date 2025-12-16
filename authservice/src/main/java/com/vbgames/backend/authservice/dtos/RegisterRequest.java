package com.vbgames.backend.authservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Debe ser una dirección de correo electrónico válida"
    )
    @NotNull(message = "El email no puede estar vacio")
    private String email;

    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
        message = "La contraseña debe tener al menos una minúscula, una mayúscula, un número y mínimo 8 caracteres"
    )
    @NotNull(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "Confirmar la contraseña es obligatorio")
    private String confirmPassword;
}
