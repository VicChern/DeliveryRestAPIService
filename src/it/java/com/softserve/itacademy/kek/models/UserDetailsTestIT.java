package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.UserDetailsRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.TransactionSystemException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_512;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getSimpleUserDetailsWithValidFields;
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
        User user = getOrdinaryUser(1);
        UserDetails userDetails = getSimpleUserDetailsWithValidFields();
        userDetails.setUser(user);
        user.setUserDetails(userDetails);

        //when
        userRepository.save(user);

        //then
        assertEquals(1, userRepository.findAll().spliterator().estimateSize());
        assertEquals(1, userDetailsRepository.findAll().spliterator().estimateSize());
        assertTrue(userRepository.existsById(user.getIdUser()));
        assertTrue(userDetailsRepository.existsById(userDetails.getUser().getIdUser()));
        assertEquals(user.getIdUser(), userDetails.getIdUser());
    }


    //==================================================== payload =====================================================

    @Test(description = "Test UserDetails_02. Should throw TransactionSystemException when save userDetails with payload field length more than " + MAX_LENGTH_4096,
            expectedExceptions = TransactionSystemException.class,
            expectedExceptionsMessageRegExp = "Could not commit JPA transaction; .*")
    public void testUserDetailsIsNotSavedWithPayloadMoreThanMaxLength() {
        //given
        User user = getOrdinaryUser(1);
        UserDetails userDetails = getSimpleUserDetailsWithValidFields();
        userDetails.setPayload(getRandomLetterString(MAX_LENGTH_4096 + 1 + new Random().nextInt(50)));
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
        User user = getOrdinaryUser(1);
        UserDetails userDetails = getSimpleUserDetailsWithValidFields();
        userDetails.setImageUrl(getRandomLetterString(MAX_LENGTH_512 + 1 + new Random().nextInt(50)));
        userDetails.setUser(user);
        user.setUserDetails(userDetails);

        //when
        userRepository.save(user);
    }
}
