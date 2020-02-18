package com.softserve.itacademy.kek.models;

import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.IdentityType;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.repositories.IdentityTypeRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createIdentity;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Integration tests for {@link Identity} constraints (save identity by saving user).
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class IdentityTestIT extends AbstractTestNGSpringContextTests {

    private User user;
    private Identity identity;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IdentityRepository identityRepository;
    @Autowired
    private IdentityTypeRepository identityTypeRepository;

    @BeforeMethod
    void setUp() {
        user = createOrdinaryUser(1);
        identity = createIdentity();
    }

    @AfterMethod
    void tearDown() {
        userRepository.deleteAll();
        identityRepository.deleteAll();
        identityTypeRepository.deleteAll();
    }


    @Test(description = "Test Identity_01. Should saves user with valid identity fields.")
    public void testUserIsSavedWithValidIdentityFields() {
        //given
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

    @Test(description = "Test Identity_01. Should saves identity with valid fields for existing in db user.")
    public void testIdentityIsSavedWithExistingInDbUserAndValidIdentityFields() {

        //given
        User savedUser = userRepository.save(user);
        assertNotNull(userRepository.findById(savedUser.getIdUser()).orElse(null));

        identity.setUser(savedUser);

        //when
       identityRepository.save(identity);

        //then
        assertEquals(1, identityRepository.findAll().spliterator().estimateSize());
        assertEquals(1, identityTypeRepository.findAll().spliterator().estimateSize());

        Optional<Identity> identityOptional = identityRepository.findById(identity.getIdIdentity());
        assertTrue(identityOptional.isPresent());
        assertEquals(identityOptional.get(), identity);

        Optional<IdentityType> identityTypeOptional = identityTypeRepository.findById(identity.getIdentityType().getIdIdentityType());
        assertTrue(identityTypeOptional.isPresent());
    }


    //================================================ identityType name ===============================================
    @Test(description = "Test Identity_02. Should throw ConstraintViolationException when saves user with null identityType name field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullIdentityTypeName() {
        //given
        identity.getIdentityType().setName(null);
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test Identity_03. Should throw ConstraintViolationException when saves user with empty identityType name field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithEmptyIdentityTypeName() {
        //given
        identity.getIdentityType().setName("");
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test Identity_04. Should throw ConstraintViolationException when saves user with a name field length of identityType more than " + MAX_LENGTH_256,
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

    @Test(description = "Test Identity_05. Should throw DataIntegrityViolationException when saves user with not unique identityType name field",
            expectedExceptions = DataIntegrityViolationException.class,
            expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void testUserIsSavedWithUniqueIdentityTypeName() {
        //given
        User user1 = user;
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
    @Test(description = "Test Identity_06. Should throw ConstraintViolationException when saves user with null identity payload field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullIdentityPayload() {
        //given
        identity.setPayload(null);
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test Identity_07. Should throw ConstraintViolationException when saves user with empty identity payload field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithEmptyIdentityPayload() {
        //given
        identity.getIdentityType().setName("");
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }

    @Test(description = "Test Identity_08. Should throw ConstraintViolationException when saves user with payload field length of identity more than " + MAX_LENGTH_4096,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWitIdentityPayloadMoreThanMaxLength() {
        //given
        String payload = createRandomLetterString(MAX_LENGTH_4096 + 1 + new Random().nextInt(50));
        identity.setPayload(payload);
        user.addIdentity(identity);

        //when
        userRepository.save(user);
    }
}
