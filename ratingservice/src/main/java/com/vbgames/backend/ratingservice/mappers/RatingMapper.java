package com.vbgames.backend.ratingservice.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.vbgames.backend.ratingservice.dtos.GameRatingResponse;
import com.vbgames.backend.ratingservice.dtos.UserRatingResponse;
import com.vbgames.backend.ratingservice.entities.Rating;

@Mapper(componentModel = "spring", uses = { UserMapper.class, GameMapper.class })
public interface RatingMapper {

    List<GameRatingResponse> toGameRatingResponseList(List<Rating> ratings);

    List<UserRatingResponse> toUserRatingResponseList(List<Rating> ratings);
}