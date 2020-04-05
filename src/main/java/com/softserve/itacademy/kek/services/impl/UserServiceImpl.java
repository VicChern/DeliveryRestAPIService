package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.TenantServiceException;
import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.IUserDetails;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private TenantRepository tenantRepository;
    private ActorRepository actorRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TenantRepository tenantRepository, ActorRepository actorRepository) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.actorRepository = actorRepository;
    }

    @Transactional
    @Override
    public IUser create(IUser userData) throws UserServiceException {
        logger.info("Insert User into DB: {}", userData);

        final User user = new User();

        user.setGuid(UUID.randomUUID());
        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());

        final UserDetails details = new UserDetails();
        user.setUserDetails(details);

        final IUserDetails detailsData = userData.getUserDetails();
        if (detailsData != null) {
            details.setImageUrl(detailsData.getImageUrl());
            details.setPayload(detailsData.getPayload());
        }

        try {
            final User insertedUser = userRepository.saveAndFlush(user);

            logger.debug("User was inserted into DB: {}", insertedUser);

            return insertedUser;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting User into DB: " + user, ex);
            throw new UserServiceException("An error occurred while inserting user", ex);
        }
    }

    @Transactional
    @Override
    public IUser update(IUser userData) throws UserServiceException {
        logger.info("Update User in DB: {}", userData);

        final User user = internalGetByGuid(userData.getGuid());

        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());

        final IUserDetails detailsData = userData.getUserDetails();
        if (detailsData != null) {
            final UserDetails details = new UserDetails();
            user.setUserDetails(details);

            details.setIdUser(user.getIdUser());
            details.setImageUrl(detailsData.getImageUrl());
            details.setPayload(detailsData.getPayload());
        }

        try {
            final User updatedUser = userRepository.saveAndFlush(user);

            logger.debug("User was updated in DB: {}", updatedUser);

            return updatedUser;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating User in DB: " + user, ex);
            throw new UserServiceException("An error occurred while updating user", ex);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws UserServiceException {
        logger.info("Delete User from DB by guid: {}", guid);

        final User user = internalGetByGuid(guid);

        try {
            userRepository.deleteById(user.getIdUser());
            userRepository.flush();

            logger.debug("User was deleted from DB: {}", user);
        } catch (DataAccessException ex) {
            logger.error("Error while deleting User from DB: " + user, ex);
            throw new UserServiceException("An error occurred while deleting user", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IUser getByGuid(UUID guid) throws UserServiceException {
        logger.info("Get User from DB by guid: {}", guid);
        return internalGetByGuid(guid);
    }

    @Transactional(readOnly = true)
    @Override
    public IUser getByEmail(String email) throws UserServiceException {
        logger.info("Getting User from DB by email: email = {}", email);

        final User userFromDB = userRepository.findByEmail(email).orElseThrow(()->{
        logger.error("User was not received from DB: by email {}", email);
        throw new UserServiceException("An error occurred while getting the user from " +
                "the Database by email {}" + email, new NoSuchElementException());
        });

        logger.debug("User was found in DB: {}", userFromDB);

        return userFromDB;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IUser> getAll() throws UserServiceException {
        logger.info("Get all Users from DB");

        try {
            final List<? extends IUser> users = userRepository.findAll();
            return (List<IUser>) users;
        } catch (DataAccessException ex) {
            logger.error("Error while getting all Users from DB", ex);
            throw new UserServiceException("An error occurred while getting users", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<IUser> getAll(Pageable pageable) throws UserServiceException {
        logger.info("Getting a page of Users from DB: {}", pageable);

        try {
            final Page<? extends IUser> users = userRepository.findAll(pageable);
            return (Page<IUser>) users;
        } catch (DataAccessException ex) {
            logger.error("Error while getting a page of Users from DB: " + pageable, ex);
            throw new UserServiceException("An error occurred while getting users", ex);
        }
    }

    private User internalGetByGuid(UUID guid) throws UserServiceException {
        logger.debug("Internal Get User from DB by guid: {}", guid);

        try {
            final User user = userRepository.findByGuid(guid).orElseThrow(
                    () -> {
                        logger.error("User wasn't found in the database");
                        return new UserServiceException("User was not found in database for guid: " + guid, new NoSuchElementException());
                    }
            );
            return user;

        } catch (DataAccessException ex) {
            logger.error("Error while getting User from DB by guid " + guid, ex);
            throw new UserServiceException("An error occurred while getting user", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<? extends GrantedAuthority> getUserAuthorities(String email) throws UserServiceException {
        logger.info("Find User in DB: email = {}", email);

        final List<GrantedAuthority> authorityList = new ArrayList<>();

        final IUser user = userRepository.findByEmail(email).get();

        if (userRepository.findByEmail(email).isEmpty()) {
            logger.warn("User wasn't found in DB: email = {}", email);
            return authorityList;
        } else {
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        try {
            final ITenant tenant = tenantRepository.findByTenantOwner(user).orElse(null);
            if (tenantRepository.findByTenantOwner(user).isPresent()) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_TENANT"));
            }

        } catch (DataAccessException ex) {
            logger.error("Error while getting Tenant from DB by user " + user, ex);
            throw new TenantServiceException("An error occurred while getting tenant by user", ex);
        }

        final boolean existActor = actorRepository.findByUser(user).isPresent();
        if (existActor) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_ACTOR"));
        }
        return authorityList;
    }

}
