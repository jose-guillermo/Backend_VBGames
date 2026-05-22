package com.vbgames.backend.matchservice.dtos;

import java.util.List;
import java.util.UUID;

import com.vbgames.backend.common.dto.Action;
import com.vbgames.backend.common.validators.ValidAction;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveRequest {

    @Min(1)
    @NotNull
    private Short turn;

    @NotNull
    private UUID matchId;

    @NotNull
    @Size(min = 1)
    private List<@Valid @ValidAction Action> actions;

    @NotNull
    private Boolean gameOver;
}
