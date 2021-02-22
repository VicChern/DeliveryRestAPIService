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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.mappers.IUserDetailsMapper;
import com.softserve.itacademy.kek.mappers.IUserMapper;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.IUserDetails;
import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IIdentityService;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final ActorRepository actorRepository;
    private final IIdentityService identityService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           TenantRepository tenantRepository,
                           ActorRepository actorRepository,
                           IIdentityService identityService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.actorRepository = actorRepository;
        this.identityService = identityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public IUser create(IUser user) throws UserServiceException {
        logger.info("Insert user into DB: {}", user);

        try {
            final User actualUser = IUserMapper.INSTANCE.toUser(user);
            actualUser.setGuid(UUID.randomUUID());

            UserDetails actualDetails = new UserDetails();
            IUserDetails details = user.getUserDetails();

            if (details != null) actualDetails = IUserDetailsMapper.INSTANCE.toUserDetails(details);

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
    public IUser create(IUser user, String key) throws UserServiceException {
        logger.info("User registration: email = {}", user);

        final Optional<User> userFromDb = internalGetByEmail(user.getEmail());

        if (userFromDb.isPresent()) {
            logger.error("User already exists in DB: email = {}", user.getEmail());
            throw new UserServiceException("User already exists", HttpStatus.FORBIDDEN.value());
        }

        final IUser createdUser = create(user);

        try {
            identityService.create(createdUser.getGuid(), IdentityTypeEnum.KEY, passwordEncoder.encode(key));

            logger.debug("Identity was inserted into DB for user: {}", createdUser.getEmail());
        } catch (Exception ex) {
            logger.error("Error while register user", ex);
            throw new UserServiceException("An error occurred while register user", ex);
        }

        logger.info("User was added into DB: {}", createdUser);

        return createdUser;
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
    public void deleteAll() throws UserServiceException {
        logger.info("Delete all users except admin");
        try {
            userRepository.deleteAll();

            logger.debug("All users was deleted from DB");
        } catch (Exception ex) {
            logger.error("Error while deleting all users from DB:");
            throw new UserServiceException("An error occured while deleting all users", ex);
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

        final Optional<User> userFromDb = internalGetByEmail(email);

        return userFromDb.orElseThrow(() -> new UserServiceException("User was not found"));
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
    public Collection<? extends GrantedAuthority> getAuthorities(String email) throws UserServiceException {
        logger.info("Get user authorities by email: {}", email);

        final User user = (User) getByEmail(email);

        try {
            final List<GrantedAuthority> authorityList = new ArrayList<>();

            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

            logger.debug("USER role was checked");

            final Optional<Tenant> tenant = tenantRepository.findByTenantOwner(user);
            if (tenant.isPresent()) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_TENANT"));
            }

            logger.debug("TENANT role was checked");

            final Optional<Actor> actor = actorRepository.findByUser(user);
            if (actor.isPresent()) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_ACTOR"));
            }

            logger.debug("ACTOR role was checked");

            if (user.getIdUser() == 1) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            logger.debug("ADMIN role was checked");

            return authorityList;

        } catch (Exception ex) {
            logger.error("Error while getting user authorities", ex);
            throw new UserServiceException("An error occurred while getting user data", ex);
        }
    }

    private Optional<User> internalGetByEmail(String email) {
        try {
            final Optional<User> userFromDb = userRepository.findByEmail(email);

            logger.debug("User was gotten from DB by email: {}", userFromDb);

            return userFromDb;
        } catch (Exception ex) {
            logger.error("Error while getting user from DB by email: " + email, ex);
            throw new UserServiceException("An error occurred while getting user", ex);
        }

    }
}
