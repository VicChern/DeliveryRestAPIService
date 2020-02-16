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
import com.softserve.itacademy.kek.models.impl.IdentityType;
import com.softserve.itacademy.kek.repositories.IdentityTypeRepository;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createIdentityType;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Integration tests for {@link IdentityType} constraints.
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class IdentityTypeTestIT extends AbstractTestNGSpringContextTests {

    private IdentityType identityType;

    @Autowired
    private IdentityTypeRepository identityTypeRepository;

    @BeforeMethod
    void setUp() {
        identityType = createIdentityType();
    }

    @AfterMethod
    void tearDown() {
        identityTypeRepository.deleteAll();
    }


    @Test(groups = {"integration-tests"}, description = "Test IdentityType_01. Should saves identityType with valid fields.")
    public void testIdentityTypeIsSavedWithValidFields() {
        //when
        identityTypeRepository.save(identityType);

        //then
        assertEquals(1, identityTypeRepository.findAll().spliterator().estimateSize());

        Optional<IdentityType> savedIdentityTypeOptional = identityTypeRepository.findById(identityType.getIdIdentityType());
        assertNotNull(savedIdentityTypeOptional.orElse(null));
    }

    //====================================================== name ======================================================
    @Test(groups = {"integration-tests"}, description = "Test IdentityType_02. Should throw ConstraintViolationException when saves identityType with null name field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testUserIsNotSavedWithNullIdentityTypeName() {
        //given
        identityType.setName(null);

        //when
        identityTypeRepository.save(identityType);
    }

    @Test(groups = {"integration-tests"}, description = "Test IdentityType_03. Should throw ConstraintViolationException when saves identityType with empty name field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testIdentityTypeIsNotSavedWithEmptyName() {
        //given
        identityType.setName("");

        //when
        identityTypeRepository.save(identityType);
    }

    @Test(groups = {"integration-tests"}, description = "Test IdentityType_04. Should throw ConstraintViolationException when save identityType with name field length more than " + MAX_LENGTH_256,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testIdentityTypeIsNotSavedWithNameMoreThanMaxLength() {
        //given
        String name = createRandomLetterString(MAX_LENGTH_256 + 1 + new Random().nextInt(50));
        identityType.setName(name);

        //when
        identityTypeRepository.save(identityType);
    }

    @Test(groups = {"integration-tests"}, description = "Test IdentityType_05. Should throw DataIntegrityViolationException when saves identityType with not unique name field",
            expectedExceptions = DataIntegrityViolationException.class,
            expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void testIdentityTypeIsSavedWithUniqueName() {
        //given
        IdentityType identityType1 = identityType;
        IdentityType identityType2 = createIdentityType();

        //      makes identityType names equals
        identityType1.setName(identityType2.getName());

        //when
        identityTypeRepository.save(identityType1);
        identityTypeRepository.save(identityType2);
    }

}
