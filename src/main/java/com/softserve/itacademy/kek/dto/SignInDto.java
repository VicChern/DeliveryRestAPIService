package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

import com.softserve.itacademy.kek.controller.utils.Validation.ValidEmail;
import com.softserve.itacademy.kek.controller.utils.Validation.ValidPassword;

public class SignInDto {

    @NotNull
    @NotEmpty
    @Size(max = 256)
    @ValidEmail
    String email;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 32)
    @ValidPassword
    String password;

    public SignInDto() {
    }

    public SignInDto(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ( !(o instanceof SignInDto) ) return false;
        SignInDto signInDto = (SignInDto) o;
        return Objects.equals(email, signInDto.email) &&
                Objects.equals(password, signInDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "SignInDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
