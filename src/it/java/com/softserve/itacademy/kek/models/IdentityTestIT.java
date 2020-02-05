package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.repositories.IdentityTypeRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Random;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createIdentity;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Integration tests for {@link Identity} constraints.
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})

public class IdentityTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IdentityRepository identityRepository;
    @Autowired
    private IdentityTypeRepository identityTypeRepository;

    @BeforeMethod
    void setUp() {
        userRepository.deleteAll();
        identityRepository.deleteAll();
        identityTypeRepository.deleteAll();
    }
//
//    @AfterMethod
//    void tearDown() {
//        userRepository.deleteAll();
//        identityRepository.deleteAll();
//    }


    @Test(description = "Test Identity_01. Should save identity with valid fields.")
    public void testUserIsSavedWithValidIdentityFields() {
        //given
        User user = createOrdinaryUser(1);
        Identity identity = createIdentity();
        user.addIdentity(identity);

        //when
        userRepository.save(user);

        //then
        assertEquals(1, userRepository.findAll().spliterator().estimateSize());
        assertEquals(1, identityRepository.findAll().spliterator().estimateSize());
        assertEquals(1, identityTypeRepository.findAll().spliterator().estimateSize());

        Optional<User> savedUserOptional = userRepository.findById(user.getIdUser());
        assertTrue(savedUserOptional.isPresent());

        assertTrue(identityRepository.existsById(identity.getIdIdentity()));
        assertTrue(identityTypeRepository.existsById(identity.getIdentityType().getIdIdentityType()));

        assertEquals(identityRepository.findById(identity.getIdIdentity()).get(), identity);
        assertEquals(identity.getUser(), user);
    }


    //================================================ identityType name ===============================================
    @Test(description = "Test Identity_02. Should throw ConstraintViolationException when save user with null identityType name field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullIdentityTypeName() {
        //given
        User user = createOrdinaryUser(1);
        Identity identity = createIdentity();
        identity.getIdentityType().setName(null);
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test Identity_03. Should throw ConstraintViolationException when save user with empty identityType name field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithEmptyIdentityTypeName() {
        //given
        User user = createOrdinaryUser(1);
        Identity identity = createIdentity();
        identity.getIdentityType().setName("");
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test Identity_04. Should throw ConstraintViolationException when save user with identityType name field length more than " + MAX_LENGTH_256,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWitIdentityTypeNameMoreThanMaxLength() {
        //given
        String name = createRandomLetterString(MAX_LENGTH_256 + 1 + new Random().nextInt(50));
        User user = createOrdinaryUser(1);
        Identity identity = createIdentity();
        identity.getIdentityType().setName(name);
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test Identity_05. Should throw DataIntegrityViolationException when save user with not unique identityType name field",
            expectedExceptions = DataIntegrityViolationException.class,
            expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void testUserIsSavedWithUniqueIdentityTypeName() {
        //given
        User user1 = createOrdinaryUser(1);
        User user2 = createOrdinaryUser(2);

        Identity identity1 = createIdentity();
        Identity identity2 = createIdentity();

        //      makes identityType names equals
        identity2.getIdentityType().setName(identity1.getIdentityType().getName());

        user1.addIdentity(identity1);
        user2.addIdentity(identity2);

        //when
        User savedUser1 = userRepository.save(user1);
        assertEquals(savedUser1, user1);
        userRepository.save(user2);
    }


    //================================================ payload ===============================================
    @Test(description = "Test Identity_06. Should throw ConstraintViolationException when save user with null identity payload field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullIdentityPayload() {
        //given
        User user = createOrdinaryUser(1);
        Identity identity = createIdentity();
        identity.setPayload(null);
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test Identity_07. Should throw ConstraintViolationException when save user with empty identity payload field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithEmptyIdentityPayload() {
        //given
        User user = createOrdinaryUser(1);
        Identity identity = createIdentity();
        identity.getIdentityType().setName("");
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test Identity_08. Should throw ConstraintViolationException when save user with identity payload field length more than " + MAX_LENGTH_4096,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWitIdentityPayloadMoreThanMaxLength() {
        //given
        String payload = createRandomLetterString(MAX_LENGTH_4096 + 1 + new Random().nextInt(50));
        User user = createOrdinaryUser(1);
        Identity identity = createIdentity();
        identity.setPayload(payload);
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }
}
