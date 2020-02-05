package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.UserDetailsRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.TransactionSystemException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_512;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createSimpleUserDetailsWithValidFields;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Integration tests for {@link UserDetails} constraints.
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserDetailsTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @BeforeMethod
    void setUp() {
        userRepository.deleteAll();
        userDetailsRepository.deleteAll();
    }
//
//    @AfterMethod
//    void tearDown() {
//        userRepository.deleteAll();
//        userDetailsRepository.deleteAll();
//    }


    @Test(description = "Test UserDetails_01. Should save userDetails with valid fields.")
    public void testUserDetailsIsSavedWithValidFields() {
        //given
        User user1 = createOrdinaryUser(1);
        UserDetails userDetails1 = createSimpleUserDetailsWithValidFields();
        userDetails1.setUser(user1);
        user1.setUserDetails(userDetails1);

        User user2 = createOrdinaryUser(2);

        User user3 = createOrdinaryUser(3);
        UserDetails userDetails3 = createSimpleUserDetailsWithValidFields();
        userDetails3.setUser(user3);
        user3.setUserDetails(userDetails3);

        //when
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        //then
        assertEquals(3, userRepository.findAll().spliterator().estimateSize());
        assertEquals(2, userDetailsRepository.findAll().spliterator().estimateSize());

        assertTrue(userRepository.existsById(user1.getIdUser()));
        assertTrue(userRepository.existsById(user2.getIdUser()));
        assertTrue(userRepository.existsById(user3.getIdUser()));
        assertTrue(userDetailsRepository.existsById(userDetails1.getIdUser()));
        assertTrue(userDetailsRepository.existsById(userDetails3.getIdUser()));

        assertEquals(user1.getIdUser(), userDetails1.getIdUser());
        assertEquals(user3.getIdUser(), userDetails3.getIdUser());

        assertEquals(user1, userDetails1.getUser());
        assertEquals(user3, userDetails3.getUser());
    }


    //==================================================== payload =====================================================

    @Test(description = "Test UserDetails_02. Should throw TransactionSystemException when save userDetails with payload field length more than " + MAX_LENGTH_4096,
            expectedExceptions = TransactionSystemException.class,
            expectedExceptionsMessageRegExp = "Could not commit JPA transaction; .*")
    public void testUserDetailsIsNotSavedWithPayloadMoreThanMaxLength() {
        //given
        User user = createOrdinaryUser(1);
        UserDetails userDetails = createSimpleUserDetailsWithValidFields();
        userDetails.setPayload(createRandomLetterString(MAX_LENGTH_4096 + 1 + new Random().nextInt(50)));
        userDetails.setUser(user);
        user.setUserDetails(userDetails);

        //when
        userRepository.save(user);

        //than
        assertEquals(0, userRepository.findAll().spliterator().estimateSize());
        assertEquals(0, userDetailsRepository.findAll().spliterator().estimateSize());
    }


    //==================================================== imageUrl ====================================================
    @Test(description = "Test UserDetails_03. Should throw TransactionSystemException when save userDetails with imageUrl field length more than " + MAX_LENGTH_512,
            expectedExceptions = TransactionSystemException.class,
            expectedExceptionsMessageRegExp = "Could not commit JPA transaction; .*")
    public void testUserDetailsIsNotSavedWithImageUrlMoreThanMaxLength() {
        //given
        User user = createOrdinaryUser(1);
        UserDetails userDetails = createSimpleUserDetailsWithValidFields();
        userDetails.setImageUrl(createRandomLetterString(MAX_LENGTH_512 + 1 + new Random().nextInt(50)));
        userDetails.setUser(user);
        user.setUserDetails(userDetails);

        //when
        userRepository.save(user);
    }
}
