package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

//TODO: Add Logger
@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserRepositoryTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository repository;

    @BeforeMethod
    void setUp() {
        repository.deleteAll();
    }



    //====================================================== guid ======================================================
    @Test
    public void addUserWithGuidSuccess() {
        //given
        User user = getOrdinaryUser(1);

        //when
        assertEquals(0, repository.findAll().spliterator().estimateSize());
        User savedUser = repository.save(user);

        //then
        assertEquals(1, repository.findAll().spliterator().estimateSize());
        user.setIdUser(savedUser.getIdUser());
        assertEquals(user, savedUser);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void addUserThrowsExceptionWhenGuidIsNull() {
        //given
        User user = getSimpleUser(
                null,
                "name",
                "nickname",
                "email@gmail.com",
                "380-50-444-55-55");

        //when
        repository.save(user);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void addUserThrowsExceptionWhenNotUniqueGuid() {
        //given
        User user1 = getOrdinaryUser(1);
        User user2 = getOrdinaryUser(2);
        user2.setGuid(user1.getGuid());
        assertEquals(user1.getGuid(), user2.getGuid());

        //when
        User savedUser1 = repository.save(user1);
        assertEquals(savedUser1, user1);
        repository.save(user2);
    }


    //====================================================== name ======================================================

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void addUserThrowsExceptionWhenNameIsNull() {
        //given
        User user = getSimpleUser(
                UUID.randomUUID(),
                null,
                "nickname",
                "email@gmail.com",
                "380-50-444-55-55");

        //when
        repository.save(user);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void addUserThrowsExceptionWhenNameIsMoreThan256() {
        String name = getRandomString(257);
        //given
        User user = getSimpleUser(
                UUID.randomUUID(),
                name,
                "nickname",
                "email@gmail.com",
                "380-50-444-55-55");

        //when
        repository.save(user);
    }

    //====================================================== nickname ======================================================
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void addUserThrowsExceptionWhenNicknameIsNull() {
        //given
        User user = getSimpleUser(
                UUID.randomUUID(),
                "name",
                null,
                "email@gmail.com",
                "380-50-444-55-55");

        //when
        repository.save(user);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void addUserThrowsExceptionWhenNotUniqueNickname() {
        //given
        User user1 = getOrdinaryUser(1);
        User user2 = getOrdinaryUser(2);
        user2.setNickname(user1.getNickname());
        assertEquals(user1.getNickname(), user2.getNickname());

        //when
        User savedUser1 = repository.save(user1);
        assertEquals(savedUser1, user1);
        repository.save(user2);
    }



    //================================================== util methods ==================================================
    private User getOrdinaryUser(int i) {
        return getSimpleUser(
                UUID.randomUUID(),
                "name" + i,
                "nickname" + i,
                "email" + i + "@gmail.com",
                "380-50-444-55-55" + 1);
    }

    private User getSimpleUser(UUID guid, String name, String nickName, String email, String phoneNumber) {
        User user = new User();
        user.setName(name);
        user.setGuid(guid);
        user.setNickname(nickName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        return user;
    }

    private String getRandomString(int stringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}