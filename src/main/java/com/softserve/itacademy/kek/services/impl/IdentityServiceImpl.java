package com.softserve.itacademy.kek.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.models.IIdentity;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.services.IIdentityService;

@Service
public class IdentityServiceImpl implements IIdentityService {

    private final IdentityRepository identityRepository;

    @Autowired
    public IdentityServiceImpl(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    @Transactional
    @Override
    public IIdentity savePassword(User user, String password) {
        Identity identity = new Identity(user, password);

        identityRepository.save(identity);

        return identity;
    }
}
