package com.softserve.itacademy.kek.models;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.TransactionSystemException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;
import com.softserve.itacademy.kek.repositories.UserRepository;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_512;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createSimpleUserDetailsWithValidFields;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Integration tests for {@link UserDetails} constraints.
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserDetailsTestIT extends AbstractTestNGSpringContextTests {

    private User user;
    private UserDetails userDetails;

    @Autowired
    private UserRepository userRepository;

    @BeforeMethod(groups = {"integration-tests"})
    void setUp() {
        user = createOrdinaryUser(1);
        userDetails = createSimpleUserDetailsWithValidFields();

        user.setUserDetails(userDetails);
        userDetails.setUser(user);
    }

    @AfterMethod(groups = {"integration-tests"})
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test(groups = {"integration-tests"},
            description = "Test UserDetails_01. Should saves user with valid userDetails fields.")
    public void testUserDetailsIsSavedWithValidFields() {
        //when
        userRepository.save(user);

        //then
        assertEquals(1, userRepository.findAll().spliterator().estimateSize());

        Optional<User> userOptional = userRepository.findById(user.getIdUser());
        assertNotNull(userOptional.orElse(null));

       UserDetails userDetails = (UserDetails) userOptional.get().getUserDetails();

        assertEquals(userOptional.get().getIdUser(), userDetails.getIdUser());
    }


    //==================================================== payload =====================================================

    @Test(groups = {"integration-tests"},
            description = "Test UserDetails_02. Should throw TransactionSystemException when saves user with a payload field length of userDetails more than " + MAX_LENGTH_4096,
            expectedExceptions = TransactionSystemException.class,
            expectedExceptionsMessageRegExp = "Could not commit JPA transaction; .*")
    public void testUserDetailsIsNotSavedWithPayloadMoreThanMaxLength() {
        //given
        String payload = createRandomLetterString(MAX_LENGTH_4096 + 1 + new Random().nextInt(50));
        userDetails.setPayload(payload);

        //when
        userRepository.save(user);
    }


    //==================================================== imageUrl ====================================================
    @Test(groups = {"integration-tests"},
            description = "Test UserDetails_03. Should throw TransactionSystemException when saves user with a imageUrl field length of userDetails more than " + MAX_LENGTH_512,
            expectedExceptions = TransactionSystemException.class,
            expectedExceptionsMessageRegExp = "Could not commit JPA transaction; .*")
    public void testUserDetailsIsNotSavedWithImageUrlMoreThanMaxLength() {
        //given
        String imageUrl = createRandomLetterString(MAX_LENGTH_512 + 1 + new Random().nextInt(50));
        userDetails.setImageUrl(imageUrl);

        //when
        userRepository.save(user);
    }
}
