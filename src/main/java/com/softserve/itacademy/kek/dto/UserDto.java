package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.softserve.itacademy.kek.models.IUser;

public class UserDto implements IUser, UserDetails {
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
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
}
