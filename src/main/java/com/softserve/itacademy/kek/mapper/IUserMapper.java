package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.models.IUser;

public interface IUserMapper {

    UserDto fromIUser(IUser user);
}
