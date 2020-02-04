package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.Random;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;

    @AfterMethod
    void tearDown() {
        userRepository.deleteAll();
    }


    //====================================================== guid ======================================================
    @Test
    public void whenGuidIsValidThanSuccess() {
        //given
        User user = getOrdinaryUser(1);

        //when
        assertEquals(0, userRepository.findAll().spliterator().estimateSize());
        User savedUser = userRepository.save(user);

        //then
        assertTrue(userRepository.existsById(savedUser.getIdUser()));
        user.setIdUser(savedUser.getIdUser());
        assertEquals(user, savedUser);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenGuidIsNullThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setGuid(null);

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void whenGuidIsNotUniqueThanThrowsException() {
        //given
        User user1 = getOrdinaryUser(1);
        User user2 = getOrdinaryUser(2);
        user2.setGuid(user1.getGuid());
        assertEquals(user1.getGuid(), user2.getGuid());

        //when
        User savedUser1 = userRepository.save(user1);
        assertEquals(savedUser1, user1);
        userRepository.save(user2);
    }


    //====================================================== name ======================================================

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenNameIsNullThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setName(null);

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenNameIsEmptyThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setName("");

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenNameIsMoreThan256ThanThrowsException() {
        String name = getRandomString(257);
        //given
        User user = getOrdinaryUser(1);
        user.setName(name);

        //when
        userRepository.save(user);
    }


    //====================================================== nickname ======================================================
    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenNicknameIsNullThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setNickname(null);

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenNicknameIsEmptyThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setName("");

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenNicknameIsMoreThan256ThanThrowsException() {
        String nickname = getRandomString(257);
        //given
        User user = getOrdinaryUser(1);
        user.setNickname(nickname);

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void whenNicknameIsNotUniqueThanThrowsException() {
        //given
        User user1 = getOrdinaryUser(1);
        User user2 = getOrdinaryUser(2);
        user2.setNickname(user1.getNickname());
        assertEquals(user1.getNickname(), user2.getNickname());

        //when
        User savedUser1 = userRepository.save(user1);
        assertEquals(savedUser1, user1);
        userRepository.save(user2);
    }


    //====================================================== email ======================================================
    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenEmailIsNullThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setEmail(null);

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenEmailIsEmptyThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setEmail("");

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenEmailIsMoreThan256ThanThrowsException() {
        String email = getRandomString(257);
        //given
        User user = getOrdinaryUser(1);
        user.setEmail(email);

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void whenEmailIsNotUniqueThanThrowsException() {
        //given
        User user1 = getOrdinaryUser(1);
        User user2 = getOrdinaryUser(2);
        user2.setEmail(user1.getEmail());
        assertEquals(user1.getEmail(), user2.getEmail());

        //when
        User savedUser1 = userRepository.save(user1);
        assertEquals(savedUser1, user1);
        userRepository.save(user2);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenEmailIsNotValidThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setEmail("notValidEmail");

        //when
        userRepository.save(user);
    }


    //====================================================== phoneNumber ======================================================
    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenPhoneNumberIsNullThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setPhoneNumber(null);

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenPhoneNumberIsEmptyThanThrowsException() {
        //given
        User user = getOrdinaryUser(1);
        user.setPhoneNumber("");

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed .*")
    public void whenPhoneNumberIsMoreThan256ThanThrowsException() {
        String phoneNumber = getRandomString(257);
        //given
        User user = getOrdinaryUser(1);
        user.setNickname(phoneNumber);

        //when
        userRepository.save(user);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void whenPhoneNumberIsNotUniqueThanThrowsException() {
        //given
        User user1 = getOrdinaryUser(1);
        User user2 = getOrdinaryUser(2);
        user2.setPhoneNumber(user1.getPhoneNumber());
        assertEquals(user1.getPhoneNumber(), user2.getPhoneNumber());

        //when
        User savedUser1 = userRepository.save(user1);
        assertEquals(savedUser1, user1);
        userRepository.save(user2);
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
