package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserWithImageDto {
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
    private UserDetailsWithImageDto userDetailsWithImage;

    public UserWithImageDto() {
    }

    public UserWithImageDto(UUID guid,
                            String name,
                            String nickname,
                            String email,
                            String phoneNumber,
                            UserDetailsWithImageDto userDetailsWithImage) {
        this.guid = guid;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userDetailsWithImage = userDetailsWithImage;
    }

    public UUID getGuid() {
        return guid;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserDetailsWithImageDto getUserDetailsWithImage() {
        return userDetailsWithImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserWithImageDto)) return false;
        UserWithImageDto that = (UserWithImageDto) o;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(nickname, that.nickname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(userDetailsWithImage, that.userDetailsWithImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, name, nickname, email, phoneNumber, userDetailsWithImage);
    }

    @Override
    public String toString() {
        return "UserWithImageDto{" +
                "guid=" + guid +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userDetailsWithImage=" + userDetailsWithImage +
                '}';
    }
}
