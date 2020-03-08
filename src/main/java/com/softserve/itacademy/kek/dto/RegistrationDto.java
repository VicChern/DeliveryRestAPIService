package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.softserve.itacademy.kek.controller.utils.ValidEmail;
import com.softserve.itacademy.kek.controller.utils.ValidPassword;
import com.softserve.itacademy.kek.controller.utils.ValidPhone;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.IUserDetails;

public class RegistrationDto implements IUser {
    private UUID guid;

    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    @Size(max = 256)
    private String nickname;

    @NotNull
    @NotEmpty
    @Size(max = 256)
    @ValidEmail
    private String email;

    @NotEmpty
    @JsonProperty("phone")
    @ValidPhone
    private String phoneNumber;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 32)
    @ValidPassword
    private String password;

    public RegistrationDto() {
    }

    public RegistrationDto(UUID guid, String name, String nickName, String email, String phoneNumber, String password) {
        this.guid = guid;
        this.name = name;
        this.nickname = nickName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    @Override
    public UUID getGuid() {
        return guid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public IUserDetails getUserDetails() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrationDto)) return false;
        RegistrationDto userDto = (RegistrationDto) o;
        return Objects.equals(guid, userDto.guid) &&
                Objects.equals(name, userDto.name) &&
                Objects.equals(nickname, userDto.nickname) &&
                Objects.equals(email, userDto.email) &&
                Objects.equals(phoneNumber, userDto.phoneNumber) &&
                Objects.equals(password, userDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, name, nickname, email, phoneNumber, password);
    }

    @Override
    public String toString() {
        return "RegistrationDto{" +
                "guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", nickName='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phoneNumber + '\'' +
                '}';
    }
}
