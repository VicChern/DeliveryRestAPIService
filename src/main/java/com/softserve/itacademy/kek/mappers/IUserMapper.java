package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.User;

/**
 * Interface for {@link User} mapping
 */
@Mapper(uses = IUserDetailsMapper.class)
public interface IUserMapper {

    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    /**
     * Transform {@link IUser} to {@link UserDto}
     *
     * @param user
     * @return userDto
     */
    UserDto toUserDto(IUser user);
}
