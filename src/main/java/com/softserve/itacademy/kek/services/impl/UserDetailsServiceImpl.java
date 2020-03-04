package com.softserve.itacademy.kek.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.IUserDetails;
import com.softserve.itacademy.kek.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDto userDto;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final IUser user = userRepository.findByEmail(email);
        if (user == null) {
            userDto.setEmail(email);
            return userDto;
        }

        final IUserDetails userDetails = user.getUserDetails();
        final DetailsDto detailsDto = new DetailsDto(userDetails.getPayload(), userDetails.getImageUrl());
        userDto.setGuid(user.getGuid());
        userDto.setName(user.getName());
        userDto.setNickname(user.getNickname());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setUserDetails(detailsDto);
        return userDto;
//        return new UserDto(user.getGuid(), user.getName(), user.getNickname(), user.getEmail(),
//                user.getPhoneNumber(), detailsDto);
    }
}
