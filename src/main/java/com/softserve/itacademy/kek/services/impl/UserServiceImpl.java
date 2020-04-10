package com.softserve.itacademy.kek.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.IUserDetails;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final ActorRepository actorRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TenantRepository tenantRepository, ActorRepository actorRepository) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.actorRepository = actorRepository;
    }

    @Transactional
    @Override
    public IUser create(IUser user) throws UserServiceException {
        logger.info("Insert user into DB: {}", user);

        final User actualUser = new User();

        try {
            actualUser.setGuid(UUID.randomUUID());
            actualUser.setName(user.getName());
            actualUser.setNickname(user.getNickname());
            actualUser.setEmail(user.getEmail());
            actualUser.setPhoneNumber(user.getPhoneNumber());

            final UserDetails actualDetails = new UserDetails();

            final IUserDetails details = user.getUserDetails();
            if (details != null) {
                actualDetails.setImageUrl(details.getImageUrl());
                actualDetails.setPayload(details.getPayload());
            }

            actualUser.setUserDetails(actualDetails);

            final User insertedUser = userRepository.saveAndFlush(actualUser);

            logger.debug("User was inserted into DB: {}", insertedUser);

            return insertedUser;
        } catch (Exception ex) {
            logger.error("Error while inserting user into DB: " + user, ex);
            throw new UserServiceException("An error occurred while inserting user", ex);
        }
    }

    @Transactional
    @Override
    public IUser update(IUser user) throws UserServiceException {
        logger.info("Update user in DB: {}", user);

        final User actualUser = (User) getByGuid(user.getGuid());

        try {
            actualUser.setName(user.getName());
            actualUser.setNickname(user.getNickname());
            actualUser.setEmail(user.getEmail());
            actualUser.setPhoneNumber(user.getPhoneNumber());

            final IUserDetails details = user.getUserDetails();
            if (details != null) {
                UserDetails actualDetails = (UserDetails) actualUser.getUserDetails();
                actualDetails.setImageUrl(details.getImageUrl());
                actualDetails.setPayload(details.getPayload());
            }

            final User updatedUser = userRepository.saveAndFlush(actualUser);

            logger.debug("User was updated in DB: {}", updatedUser);

            return updatedUser;
        } catch (Exception ex) {
            logger.error("Error while updating user in DB: " + actualUser, ex);
            throw new UserServiceException("An error occurred while updating user", ex);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws UserServiceException {
        logger.info("Delete user from DB by guid: {}", guid);

        final User user = (User) getByGuid(guid);

        try {
            userRepository.deleteById(user.getIdUser());
            userRepository.flush();

            logger.debug("User was deleted from DB: {}", user);
        } catch (Exception ex) {
            logger.error("Error while deleting user from DB: " + user, ex);
            throw new UserServiceException("An error occurred while deleting user", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IUser getByGuid(UUID guid) throws UserServiceException {
        logger.info("Get user from DB by guid: {}", guid);

        try {
            final User user = userRepository.findByGuid(guid)
                    .orElseThrow(() -> new NoSuchElementException("User was not found"));

            logger.debug("User was gotten from DB by guid: {}", user);

            return user;
        } catch (Exception ex) {
            logger.error("Error while getting user from DB by guid: " + guid, ex);
            throw new UserServiceException("An error occurred while getting user", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IUser getByEmail(String email) throws UserServiceException {
        logger.info("Get user from DB by email: {}", email);

        try {
            final User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NoSuchElementException("User was not found"));

            logger.debug("User was gotten from DB by email: {}", user);

            return user;
        } catch (Exception ex) {
            logger.error("Error while getting user from DB by email: " + email, ex);
            throw new UserServiceException("An error occurred while getting user", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<IUser> getAll() throws UserServiceException {
        logger.info("Get all users from DB");

        try {
            final List<? extends IUser> users = userRepository.findAll();

            logger.debug("All users were gotten from DB");

            return (List<IUser>) users;
        } catch (DataAccessException ex) {
            logger.error("Error while getting all users from DB", ex);
            throw new UserServiceException("An error occurred while getting users", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<IUser> getAll(Pageable pageable) throws UserServiceException {
        logger.info("Getting a page of users from DB: {}", pageable);

        try {
            final Page<? extends IUser> users = userRepository.findAll(pageable);

            logger.debug("A page of users was gotten from DB");

            return (Page<IUser>) users;
        } catch (Exception ex) {
            logger.error("Error while getting a page of users from DB", ex);
            throw new UserServiceException("An error occurred while getting users", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<? extends GrantedAuthority> getUserAuthorities(String email) throws UserServiceException {
        logger.info("Get user authorities: email = {}", email);

        final List<GrantedAuthority> authorityList = new ArrayList<>();

        try {
            final Optional<User> user = userRepository.findByEmail(email);

            if (user.isEmpty()) {
                logger.warn("User was not found in DB: email = {}", email);
                return authorityList;
            } else {
                authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            logger.debug("USER role was checked");

            final Optional<Tenant> tenant = tenantRepository.findByTenantOwner(user.get());
            if (tenant.isPresent()) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_TENANT"));
            }

            logger.debug("TENANT role was checked");

            final Optional<Actor> actor = actorRepository.findByUser(user.get());
            if (actor.isPresent()) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_ACTOR"));
            }

            logger.debug("ACTOR role was checked");

            return authorityList;
        } catch (Exception ex) {
            logger.error("Error while getting user authorities", ex);
            throw new UserServiceException("An error occurred while getting user data", ex);
        }
    }

    @Override
    public void validateTenant(String userEmail, String tenantGuid) throws UserServiceException {
        logger.info("Validating tenant");

        final Optional<User> user = userRepository.findByEmail(userEmail);

        final boolean valid = user.isPresent();

        if (valid) {
            logger.debug("User was found: {}", user.get());

            final boolean userIsCurrentTenant = user
                    .get()
                    .getTenant()
                    .getGuid()
                    .toString()
                    .equals(tenantGuid);

            if (userIsCurrentTenant) {
                logger.debug("Current user: {}, is current tenant with guid: {}", user.get(), tenantGuid);
            } else {
                logger.error("Current user: {}, is not current tenant with guid: {}", user, tenantGuid);
                throw new UserServiceException("Current user is not current tenant", HttpStatus.FORBIDDEN.value());
            }
        } else {
            logger.error("User with email: {}, is not valid", userEmail);
            throw new UserServiceException("Invalid email", HttpStatus.FORBIDDEN.value());
        }
    }
}
