package com.softserve.itacademy.kek.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.dto.AuthenticatedUserDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final AuthenticatedUserDto authenticatedUserDto;

    public UserDetailsServiceImpl(UserRepository userRepository, AuthenticatedUserDto authenticatedUserDto) {
        this.userRepository = userRepository;
        this.authenticatedUserDto = authenticatedUserDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final IUser user = userRepository.findByEmail(email);
        if (user == null) {
            authenticatedUserDto.setEmail(email);
            return authenticatedUserDto;
        }

        authenticatedUserDto.setGuid(user.getGuid());
        authenticatedUserDto.setName(user.getName());
        authenticatedUserDto.setNickname(user.getNickname());
        authenticatedUserDto.setEmail(user.getEmail());
        authenticatedUserDto.setPhoneNumber(user.getPhoneNumber());
        return authenticatedUserDto;
    }
}
