package com.vbgames.backend.messageservice.dtos;

import com.vbgames.backend.messageservice.enums.MessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {

    @NotNull(message = "El titulo no puede estar vacio")
    // @Size(min=)
    private String title;

    @NotBlank(message = "El cuerpo no puede estar vacio")
    private String body;

    @NotNull(message = "El tipo no puede estar vacio")
    private MessageType type;


}
