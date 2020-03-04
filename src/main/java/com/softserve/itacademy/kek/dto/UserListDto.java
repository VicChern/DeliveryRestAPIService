package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserListDto {
    @Valid
    @NotNull
    private List<UserDto> userList;

    public UserListDto() {
        this(new LinkedList<>());
    }

    public UserListDto(@Valid @NotNull List<UserDto> userList) {
        this.userList = userList;
    }

    public List<UserDto> getUserList() {
        return userList;
    }

    public UserListDto addUser(UserDto user) {
        userList.add(user);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserListDto)) return false;
        UserListDto that = (UserListDto) o;
        return Objects.equals(userList, that.userList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userList);
    }

    @Override
    public String toString() {
        return "UserListDto{"
                + "userList="
                + userList.stream().map(UserDto::toString).collect(Collectors.joining(","))
                + '}';
    }
}
