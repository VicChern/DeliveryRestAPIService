package com.softserve.itacademy.kek.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;

public class UserMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMapper.class);

    public static User toUser(IUser iUser) {
        LOGGER.info("Mapping IUser: {} to User", iUser);

        UserDetails userDetails = new UserDetails();
        userDetails.setImageUrl(iUser.getUserDetails().getImageUrl());
        userDetails.setPayload(iUser.getUserDetails().getPayload());

        User user = new User();
        user.setName(iUser.getName());
        user.setEmail(iUser.getEmail());
        user.setNickname(iUser.getNickname());
        user.setPhoneNumber(iUser.getPhoneNumber());
        user.setUserDetails(userDetails);

        LOGGER.debug("Mapped IUser: {} to User: {}", iUser, user);
        return user;
    }


    public static UserDto toUserDto(IUser iUser) {
        LOGGER.info("Mapping IUser: {} to UserDto", iUser);

        DetailsDto userDetailsDto = new DetailsDto(
                iUser.getUserDetails().getPayload(),
                iUser.getUserDetails().getImageUrl()
        );

        UserDto userDto = new UserDto(
                iUser.getGuid(),
                iUser.getName(),
                iUser.getNickname(),
                iUser.getEmail(),
                iUser.getPhoneNumber(),
                userDetailsDto);

        LOGGER.debug("Mapped IUser: {} to UserDto: {}", iUser, userDto);
        return userDto;
    }

}
