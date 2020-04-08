package com.softserve.itacademy.kek.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.dto.AuthenticatedUserDto;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    private final AuthenticatedUserDto authenticatedUserDto;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, AuthenticatedUserDto authenticatedUserDto) {
        this.userRepository = userRepository;
        this.authenticatedUserDto = authenticatedUserDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Load user data by email: {}", email);

        final Optional<User> userFromDb = userRepository.findByEmail(email);

        if (userFromDb.isEmpty()) {
            logger.debug("User was not found in DB by email: {}", email);

            authenticatedUserDto.setEmail(email);
            return authenticatedUserDto;
        }

        logger.debug("User was found in DB by email: {}", email);

        final User user = userFromDb.get();

        authenticatedUserDto.setGuid(user.getGuid());
        authenticatedUserDto.setName(user.getName());
        authenticatedUserDto.setNickname(user.getNickname());
        authenticatedUserDto.setEmail(user.getEmail());
        authenticatedUserDto.setPhoneNumber(user.getPhoneNumber());
        authenticatedUserDto.setUserDetails(user.getUserDetails());
        return authenticatedUserDto;
    }
}
