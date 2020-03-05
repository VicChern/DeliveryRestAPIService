package com.softserve.itacademy.kek.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IUserService;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserServiceTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test(groups = {"integration-tests"})
    public void createSuccess() {
        final IUser testUser = createOrdinaryUser(1);
        final IUser createdUser = userService.create(testUser);

        assertNotNull(createdUser);

        final IUser foundUser = userRepository.findByGuid(createdUser.getGuid()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(foundUser, createdUser);
    }

    @Test(groups = {"integration-tests"})
    public void updateSuccess() {
        final User testUser = createOrdinaryUser(1);
        final User createdUser = userRepository.save(testUser);

        createdUser.setNickname("integration-tests");

        final IUser updatedUser = userService.update(createdUser);

        assertNotNull(updatedUser);

        final IUser foundUser = userRepository.findByGuid(updatedUser.getGuid()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(foundUser, updatedUser);
    }

    @Test(groups = {"integration-tests"})
    public void deleteSuccess() {
        final User testUser = createOrdinaryUser(1);
        final User createdUser = userRepository.save(testUser);

        final UUID guid = createdUser.getGuid();
        userService.deleteByGuid(guid);

        final IUser foundUser = userRepository.findByGuid(guid).orElse(null);

        assertTrue(foundUser == null);
    }

    @Test(groups = {"integration-tests"})
    public void getAllSuccess() {
        final User testUser1 = createOrdinaryUser(1);
        final User createdUser1 = userRepository.save(testUser1);

        final User testUser2 = createOrdinaryUser(2);
        final User createdUser2 = userRepository.save(testUser2);

        final User testUser3 = createOrdinaryUser(3);
        final User createdUser3 = userRepository.save(testUser3);

        final List<IUser> users = userService.getAll();
        final IUser[] expected = new IUser[]{createdUser1, createdUser2, createdUser3};
        assertEqualsNoOrder(users.toArray(), expected);
    }

    @Test(groups = {"integration-tests"})
    public void getAllPageableSuccess() {
        final User testUser = createOrdinaryUser(1);
        final User createdUser = userRepository.save(testUser);

        Pageable pageable = PageRequest.of(0, 1);

        Page<IUser> users = userService.getAll(pageable);

        assertEquals(users.getTotalPages(), 1);
        assertEquals(users.getContent().get(0), createdUser);
    }
}
