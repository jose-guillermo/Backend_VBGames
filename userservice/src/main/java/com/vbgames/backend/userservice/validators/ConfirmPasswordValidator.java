package com.vbgames.backend.userservice.validators;

import org.springframework.stereotype.Component;

import com.vbgames.backend.userservice.dtos.RegisterRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, RegisterRequest>{

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        if (request.getPassword() == null || request.getConfirmPassword() == null) return true;
        if (request.getPassword().equals(request.getConfirmPassword())) return true;
        return false;
    }

}
