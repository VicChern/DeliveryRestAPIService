package com.softserve.itacademy.kek.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Load user data by email: {}", email);

        final User user;
        try {
            user = userRepository.findByEmail(email).orElseThrow();

            logger.debug("User was found in DB by email: {}", email);
        } catch (Exception ex) {
            logger.error("Error while getting user from DB by email: " + email, ex);
            throw new UsernameNotFoundException("An error occurred while getting user", ex);
        }

        authenticatedUserDto.setGuid(user.getGuid());
        authenticatedUserDto.setName(user.getName());
        authenticatedUserDto.setNickname(user.getNickname());
        authenticatedUserDto.setEmail(user.getEmail());
        authenticatedUserDto.setPhoneNumber(user.getPhoneNumber());
        authenticatedUserDto.setUserDetails(user.getUserDetails());

        return authenticatedUserDto;
    }
}
