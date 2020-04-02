package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.User;

/**
 * Interface for {@link User} mapping
 */
public interface IUserMapper {

    /**
     * Transform {@link IUser} to {@link UserDto}
     *
     * @param user
     * @return userDto
     */
    UserDto fromIUser(IUser user);
}
