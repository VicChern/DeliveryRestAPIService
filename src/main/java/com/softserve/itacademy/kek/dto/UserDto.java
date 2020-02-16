package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserDto {

    @NotNull
    @Size(max = 256)
    private String guid;

    @NotNull
    @Size(max = 256)
    private String name;

    @NotNull
    @Size(max = 256)
    private String nickname;

    @NotNull
    @Size(max = 256)
    private String email;

    @NotNull
    @Size(max = 256)
    private String phone;

    @NotNull
    @Size(max = 256)
    private DetailsDto details;

    public UserDto() {
    }

    public UserDto(String guid, String name, String nickName, String email, String phone, DetailsDto detailsDto) {
        this.guid = guid;
        this.name = name;
        this.nickname = nickName;
        this.email = email;
        this.phone = phone;
        this.details = detailsDto;
    }

    public String getGuid() {
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

    public String getPhone() {
        return phone;
    }

    public DetailsDto getDetails() {
        return details;
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
                Objects.equals(phone, userDto.phone) &&
                Objects.equals(details, userDto.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, name, nickname, email, phone, details);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", nickName='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", detailsDto=" + details +
                '}';
    }
}
