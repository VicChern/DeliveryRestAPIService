package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.softserve.itacademy.kek.models.IUser;

public class UserDto implements IUser {

    private UUID guid;

    @NotEmpty
    private String name;

    @NotNull
    @Size(max = 256)
    private String nickname;

    @NotNull
    @Size(max = 256)
    private String email;

    @NotEmpty
    @JsonProperty("phone")
    private String phoneNumber;

    @NotNull
    @JsonProperty("details")
    private DetailsDto userDetails;

    public UserDto() {
    }

    public UserDto(UUID guid, String name, String nickName, String email, String phoneNumber, DetailsDto detailsDto) {
        this.guid = guid;
        this.name = name;
        this.nickname = nickName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userDetails = detailsDto;
    }

    public UserDto(String email) {
        this.email = email;
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
    public DetailsDto getUserDetails() {
        return userDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(guid, userDto.guid) &&
                Objects.equals(name, userDto.name) &&
                Objects.equals(nickname, userDto.nickname) &&
                Objects.equals(email, userDto.email) &&
                Objects.equals(phoneNumber, userDto.phoneNumber) &&
                Objects.equals(userDetails, userDto.userDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, name, nickname, email, phoneNumber, userDetails);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", nickName='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", detailsDto=" + userDetails +
                '}';
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserDetails(DetailsDto userDetails) {
        this.userDetails = userDetails;
    }
}
