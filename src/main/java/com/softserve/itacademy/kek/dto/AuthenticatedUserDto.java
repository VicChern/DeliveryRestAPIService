package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.softserve.itacademy.kek.services.IUserService;

@Component
public class AuthenticatedUserDto implements UserDetails {

    private final IUserService userService;

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

    public AuthenticatedUserDto(IUserService userService) {
        this.userService = userService;
    }

    public IUserService getUserService() {
        return userService;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userService.getUserAuthorities(email);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
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
        if (!(o instanceof AuthenticatedUserDto)) return false;
        AuthenticatedUserDto authenticatedUserDto = (AuthenticatedUserDto) o;
        return Objects.equals(guid, authenticatedUserDto.guid) &&
                Objects.equals(name, authenticatedUserDto.name) &&
                Objects.equals(nickname, authenticatedUserDto.nickname) &&
                Objects.equals(email, authenticatedUserDto.email) &&
                Objects.equals(phoneNumber, authenticatedUserDto.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, name, nickname, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "AuthenticatedUserDto{" +
                "userService=" + userService +
                ", guid=" + guid +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
