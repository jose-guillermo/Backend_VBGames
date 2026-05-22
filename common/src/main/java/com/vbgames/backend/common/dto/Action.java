package com.vbgames.backend.common.dto;

import com.vbgames.backend.common.enums.ActionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    private ActionType type;

    private Position from;
    private Position to;

    private Position at;

    private String piece;
}
