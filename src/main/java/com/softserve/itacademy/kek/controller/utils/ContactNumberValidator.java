package com.softserve.itacademy.kek.controller.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements
        ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(ValidPhone contactNumber) {
    }

    @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        return contactField != null && contactField.matches("(^$|[0-9]{8,14})")
                && (contactField.length() > 8) && (contactField.length() < 14);
    }

}