package com.vbgames.backend.userservice.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ConfirmPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPassword {
    String message() default "Las contraseñaes no coinciden";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}