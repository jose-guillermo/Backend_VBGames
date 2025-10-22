package com.vbgames.backend.userservice.dtos;

import com.vbgames.backend.common.validators.IsUUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFavouriteGameRequest {

    @IsUUID
    private String gameId;
}
