package com.softserve.itacademy.kek.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.dto.AuthenticatedUserDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    private final AuthenticatedUserDto authenticatedUserDto;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, AuthenticatedUserDto authenticatedUserDto) {
        this.userRepository = userRepository;
        this.authenticatedUserDto = authenticatedUserDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Trying to get user name by email{}",email);
        final IUser user = userRepository.findByEmail(email).get();
        if (userRepository.findByEmail(email).isEmpty()) {
            logger.debug("User email doesn't exist");

            authenticatedUserDto.setEmail(email);
            return authenticatedUserDto;
        }
        logger.info("Adding params for new User");
        authenticatedUserDto.setGuid(user.getGuid());
        authenticatedUserDto.setName(user.getName());
        authenticatedUserDto.setNickname(user.getNickname());
        authenticatedUserDto.setEmail(user.getEmail());
        authenticatedUserDto.setPhoneNumber(user.getPhoneNumber());
        authenticatedUserDto.setUserDetails(user.getUserDetails());
        return authenticatedUserDto;
    }
}
