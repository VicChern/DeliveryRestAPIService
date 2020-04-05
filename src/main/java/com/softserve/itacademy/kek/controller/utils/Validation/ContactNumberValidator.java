package com.softserve.itacademy.kek.controller.utils.Validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import com.softserve.itacademy.kek.exception.DataValidationException;

public class ContactNumberValidator implements ConstraintValidator<ValidPhone, String> {

    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Override
    public void initialize(ValidPhone contactNumber) {
    }

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext cxt) {
        Phonenumber.PhoneNumber number = null;

        try {
           number = phoneNumberUtil.parse(contactField, null);
        } catch (NumberParseException ex) {
            throw new DataValidationException("Invalid number");
        }

        return phoneNumberUtil.isPossibleNumber(number);
    }

}