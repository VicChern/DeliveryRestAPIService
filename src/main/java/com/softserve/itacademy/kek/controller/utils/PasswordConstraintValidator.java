package com.softserve.itacademy.kek.controller.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private Pattern pattern;
    private Matcher matcher;

//    Be between 6 and 32 characters long
//    Contain at least one digit.
//    Contain at least one lower case character.
//    Contain at least one upper case character.

//    ((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,32})  - without special symbol
//    ((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%./?,%^&]).{6,32}) - Contains one special symbols in the list "@#$%./?,%^&"

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,32})";

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return validPassword(password);
    }

    private boolean validPassword(String password) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
