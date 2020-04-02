package com.softserve.itacademy.kek.mapper.impl;

import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.mapper.IUserMapper;
import com.softserve.itacademy.kek.models.IUser;

public class UserMapperImpl implements IUserMapper {
    @Override
    public UserDto fromIUser(IUser user) {
        DetailsDto userDetailsDto = new DetailsDto(user.getUserDetails().getPayload(), user.getUserDetails().getImageUrl());
        UserDto userDto = new UserDto(user.getGuid(), user.getName(), user.getNickname(), user.getEmail(),
                user.getPhoneNumber(), userDetailsDto);

        return userDto;
    }
}
