package com.vdgames.backend.gameservice.validators;

import org.springframework.stereotype.Component;

import com.vdgames.backend.gameservice.repositories.GameRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistsByNameValidation implements ConstraintValidator<ExistsByName, String> {

    private final GameRepository gameRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return gameRepository.existsByName(value);
    }

}
