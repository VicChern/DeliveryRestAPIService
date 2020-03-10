package com.softserve.itacademy.kek.services;

import java.util.Optional;
import java.util.UUID;

import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.impl.UserServiceImpl;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test User Service {@link UserServiceImpl}
 */
@Test(groups = {"unit-tests"})
public class UserServiceTest {

    private IUserService userService;

    private UserRepository userRepository;

    private TenantRepository tenantRepository;

    private ActorRepository actorRepository;

    @BeforeClass
    public void setUp() {
        userRepository = mock(UserRepository.class);
        tenantRepository = mock(TenantRepository.class);
        actorRepository = mock(ActorRepository.class);
        userService = new UserServiceImpl(userRepository, tenantRepository, actorRepository);
    }

    @AfterMethod
    void tearDown() {
        reset(userRepository);
    }

    @Test
    public void createUserSuccess() throws Exception {
        User testUser = createUserForTest(null, null);

        when(userRepository.saveAndFlush(any(User.class))).thenReturn(testUser);

        IUser createdUser = userService.create(testUser);

        ArgumentCaptor<User> acUser = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        verify(userRepository).saveAndFlush(acUser.capture());

        User actualUser = acUser.getValue();

        Assert.assertNotNull(createdUser);
        Assert.assertNotNull(actualUser.getGuid());
        Assert.assertNotNull(actualUser.getUserDetails());
    }

    @Test
    public void updateUserSuccess() throws Exception {
        Long id = 1L;
        UUID guid = UUID.randomUUID();
        User testUser = createUserForTest(id, guid);
        User foundUser = createUserForTest(id, guid);

        when(userRepository.findByGuid(guid)).thenReturn(Optional.ofNullable(foundUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(testUser);

        IUser updatedUser = userService.update(testUser);

        ArgumentCaptor<User> acUser = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        verify(userRepository).saveAndFlush(acUser.capture());

        User actualUser = acUser.getValue();

        Assert.assertNotNull(updatedUser);
        Assert.assertNotNull(actualUser.getIdUser());
        Assert.assertNotNull(actualUser.getUserDetails());
    }

    @Test
    public void deleteUserSuccess() throws Exception {
        Long id = 1L;
        UUID guid = UUID.randomUUID();
        User foundUser = createUserForTest(id, guid);

        when(userRepository.findByGuid(guid)).thenReturn(Optional.ofNullable(foundUser));

        userService.deleteByGuid(guid);

        ArgumentCaptor<Long> acUserID = ArgumentCaptor.forClass(Long.class);

        verify(userRepository, times(1)).deleteById(any(Long.class));
        verify(userRepository).deleteById(acUserID.capture());

        Long deletedUserID = acUserID.getValue();

        Assert.assertEquals(id, deletedUserID);
    }

    private User createUserForTest(Long id, UUID guid) {
        User user = createOrdinaryUser(1);
        user.setIdUser(id);
        user.setGuid(guid);
        return user;
    }
}
