package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.modelInterfaces.IUser;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.impl.UserServiceImpl;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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

    private ArgumentCaptor<User> acUser = ArgumentCaptor.forClass(User.class);

    @BeforeClass
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void createUserSuccess() throws Exception {
        User testUser = createOrdinaryUser(1);
        testUser.setIdUser(null);
        testUser.setGuid(null);

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        IUser createdUser = userService.create(testUser);

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository).save(acUser.capture());

        User actualUser = acUser.getValue();

        Assert.assertNotNull(createdUser);
        Assert.assertEquals(testUser.getName(), actualUser.getName());
        Assert.assertEquals(testUser.getNickname(), actualUser.getNickname());
        Assert.assertEquals(testUser.getEmail(), actualUser.getEmail());
        Assert.assertEquals(testUser.getPhoneNumber(), actualUser.getPhoneNumber());
        Assert.assertNotNull(actualUser.getUserDetails());
        Assert.assertEquals(testUser.getUserDetails().getImageUrl(), actualUser.getUserDetails().getImageUrl());
        Assert.assertEquals(testUser.getUserDetails().getPayload(), actualUser.getUserDetails().getPayload());
    }
}
