package com.vbgames.backend.common.validators;

import org.springframework.stereotype.Component;

import com.vbgames.backend.common.dto.Action;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ValidActionValidator implements ConstraintValidator<ValidAction, Action> {

    @Override
    public boolean isValid(Action action, ConstraintValidatorContext context) {
        if(action == null) return true;

        return switch (action.getType()) {
            case MOVE -> action.getFrom() != null && action.getTo() != null;
            case CAPTURE -> action.getAt() != null;
            case DROP -> action.getTo() != null && action.getPiece() != null;
            case PROMOTION -> action.getTo() != null && action.getPiece() != null;
        };
    }
}
