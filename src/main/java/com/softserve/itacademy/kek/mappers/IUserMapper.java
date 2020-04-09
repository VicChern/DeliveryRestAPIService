package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    @Mapping(target = "userDetails", qualifiedByName = "toDto")
    UserDto toUserDto(IUser user);

    /**
     * Transform {@link IUser} to {@link User}
     *
     * @param user
     * @return user
     */
    @Mapping(target = "userDetails", qualifiedByName = "toEntity")
    User toUser(IUser user);
}
