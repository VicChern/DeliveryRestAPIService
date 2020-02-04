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

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getRandomNumberString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Integration tests for {@link User} constraints.
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;

    @AfterMethod
    void tearDown() {
        userRepository.deleteAll();
    }


    //====================================================== guid ======================================================
    @Test(description = "Test User_01. Should save user with valid fields.")
    public void testUserIsSavedWithValidFields() {
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

    @Test(description = "Test User_02. Should throw ConstraintViolationException when save user with null guid field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullGuid() {
        //given
        User user = getOrdinaryUser(1);
        user.setGuid(null);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_03. Should throw DataIntegrityViolationException when save user with not unique guid field",
            expectedExceptions = DataIntegrityViolationException.class,
            expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void testUserIsSavedWithUniqueGuid() {
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

    @Test(description = "Test User_04. Should throw ConstraintViolationException when save user with null name field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullName() {
        //given
        User user = getOrdinaryUser(1);
        user.setName(null);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_05. Should throw ConstraintViolationException when save user with empty name field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithEmptyName() {
        //given
        User user = getOrdinaryUser(1);
        user.setName("");

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_06. Should throw ConstraintViolationException when save user with name field length more than " + MAX_LENGTH,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNameMoreThanMaxLength() {
        String name = getRandomLetterString(MAX_LENGTH + 1 + new Random().nextInt(50));
        //given
        User user = getOrdinaryUser(1);
        user.setName(name);

        //when
        userRepository.save(user);
    }


    //====================================================== nickname ======================================================
    @Test(description = "Test User_02. Should throw ConstraintViolationException when save user with null nickname field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullNickname() {
        //given
        User user = getOrdinaryUser(1);
        user.setNickname(null);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_05. Should throw ConstraintViolationException when save user with empty nickname field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithEmptyNickname() {
        //given
        User user = getOrdinaryUser(1);
        user.setName("");

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_06. Should throw ConstraintViolationException when save user with empty nickname field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNicknameMoreThanMaxLength() {
        String nickname = getRandomLetterString(MAX_LENGTH + 1 + new Random().nextInt(50));
        //given
        User user = getOrdinaryUser(1);
        user.setNickname(nickname);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_07. Should throw DataIntegrityViolationException when save user with not unique nickname field",
            expectedExceptions = DataIntegrityViolationException.class,
            expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void testUserIsSavedWithUniqueNickname() {
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
    @Test(description = "Test User_08. Should throw ConstraintViolationException when save user with null email field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullEmail() {
        //given
        User user = getOrdinaryUser(1);
        user.setEmail(null);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_09. Should throw ConstraintViolationException when save user with empty email field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithEmptyEmail() {
        //given
        User user = getOrdinaryUser(1);
        user.setEmail("");

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_10. Should throw ConstraintViolationException when save user with email field length more than " + MAX_LENGTH,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithEmailMoreThanMaxLength() {
        String email = getRandomLetterString(MAX_LENGTH + 1 + new Random().nextInt(50));
        //given
        User user = getOrdinaryUser(1);
        user.setEmail(email);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_11. Should throw DataIntegrityViolationException when save user with not unique email field",
            expectedExceptions = DataIntegrityViolationException.class,
            expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void testUserIsSavedWithUniqueEmail() {
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

    @Test(description = "Test User_12. Should throw DataIntegrityViolationException when save user with not valid email field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNotValidEmail() {
        //given
        User user = getOrdinaryUser(1);
        user.setEmail("notValidEmail");

        //when
        userRepository.save(user);
    }


    //====================================================== phoneNumber ======================================================
    @Test(description = "Test User_13. Should throw ConstraintViolationException when save user with null phoneNumber field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullPhoneNumber() {
        //given
        User user = getOrdinaryUser(1);
        user.setPhoneNumber(null);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_14. Should throw ConstraintViolationException when save user with empty phoneNumber field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithEmptyPhoneNumber() {
        //given
        User user = getOrdinaryUser(1);
        user.setPhoneNumber("");

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_15. Should throw ConstraintViolationException when save user with phoneNumber field length more than " + MAX_LENGTH,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithPhoneNumberMoreThanMaxLength() {
        String phoneNumber = getRandomNumberString(MAX_LENGTH + 1 + new Random().nextInt(50));
        //given
        User user = getOrdinaryUser(1);
        user.setNickname(phoneNumber);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test User_16. Should throw DataIntegrityViolationException when save user with not unique phoneNumber field",
            expectedExceptions = DataIntegrityViolationException.class,
            expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void testUserIsSavedWithUniquePhoneNumber() {
        //given
        User user1 = getOrdinaryUser(1);
        User user2 = getOrdinaryUser(2);
        user2.setPhoneNumber(user1.getPhoneNumber());
        assertEquals(user1.getPhoneNumber(), user2.getPhoneNumber(), "phoneNumbers is not equals");

        //when
        User savedUser1 = userRepository.save(user1);
        assertEquals(savedUser1, user1);
        userRepository.save(user2);
    }

}
