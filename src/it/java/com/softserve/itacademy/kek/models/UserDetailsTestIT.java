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

import java.util.Optional;
import java.util.Random;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_512;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createSimpleUserDetailsWithValidFields;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Integration tests for {@link UserDetails} constraints.
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserDetailsTestIT extends AbstractTestNGSpringContextTests {

    private User user;
    private UserDetails userDetails;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @BeforeMethod
    void setUp() {
        user = createOrdinaryUser(1);
        userDetails = createSimpleUserDetailsWithValidFields();

        user.setUserDetails(userDetails);
        userDetails.setUser(user);
    }

    @AfterMethod
    void tearDown() {
        userRepository.deleteAll();
        userDetailsRepository.deleteAll();
    }


    @Test(description = "Test UserDetails_01. Should saves user with valid userDetails fields.")
    public void testUserDetailsIsSavedWithValidFields() {
        //when
        userRepository.save(user);

        //then
        assertEquals(1, userRepository.findAll().spliterator().estimateSize());
        assertEquals(1, userDetailsRepository.findAll().spliterator().estimateSize());

        Optional<User> userOptional = userRepository.findById(user.getIdUser());
        assertNotNull(userOptional.orElse(null));

        Optional<UserDetails> userDetailsOptional = userDetailsRepository.findById(userDetails.getIdUser());
        assertNotNull(userDetailsOptional.orElse(null));

        assertEquals(userOptional.get().getIdUser(), userDetailsOptional.get().getIdUser());
    }


    //==================================================== payload =====================================================

    @Test(description = "Test UserDetails_02. Should throw TransactionSystemException when saves user with a payload field length of userDetails more than " + MAX_LENGTH_4096,
            expectedExceptions = TransactionSystemException.class,
            expectedExceptionsMessageRegExp = "Could not commit JPA transaction; .*")
    public void testUserDetailsIsNotSavedWithPayloadMoreThanMaxLength() {
        //given
        userDetails.setPayload(createRandomLetterString(MAX_LENGTH_4096 + 1 + new Random().nextInt(50)));

        //when
        userRepository.save(user);
    }


    //==================================================== imageUrl ====================================================
    @Test(description = "Test UserDetails_03. Should throw TransactionSystemException when saves user with a imageUrl field length of userDetails more than " + MAX_LENGTH_512,
            expectedExceptions = TransactionSystemException.class,
            expectedExceptionsMessageRegExp = "Could not commit JPA transaction; .*")
    public void testUserDetailsIsNotSavedWithImageUrlMoreThanMaxLength() {
        //given
        userDetails.setImageUrl(createRandomLetterString(MAX_LENGTH_512 + 1 + new Random().nextInt(50)));

        //when
        userRepository.save(user);
    }
}
