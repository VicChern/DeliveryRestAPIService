package com.softserve.itacademy.kek.controller.utils.Validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailConstrainsValidator implements ConstraintValidator<ValidEmail, String> {

    EmailValidator validator = EmailValidator.getInstance();

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return (validateEmail(email));
    }

    private boolean validateEmail(String email) {
        return validator.isValid(email);
    }
}
