package com.softserve.itacademy.kek.services.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IIdentityService;

@Service
public class IdentityServiceImpl implements IIdentityService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IdentityRepository identityRepository;

    @Autowired
    public IdentityServiceImpl(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    @Transactional
    @Override
    public Identity savePassword(User user, String password) {
        Identity identity = new Identity(user, password);

        identityRepository.save(identity);

        return identity;
    }
}
