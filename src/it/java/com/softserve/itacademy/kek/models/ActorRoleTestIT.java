package com.softserve.itacademy.kek.models;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getActorRole;

@Rollback
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class ActorRoleTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private ActorRoleRepository actorRoleRepository;

    private ActorRole actorRole1;
    private ActorRole actorRole2;

    @BeforeMethod
    public void setUp() {
        actorRole1 = getActorRole();
        actorRole2 = getActorRole();
    }

    @AfterMethod
    public void tearDown() {
        actorRoleRepository.deleteAll();
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testActorRoleIsNotSavedWithNameMoreThanMaxLength() {
        actorRole1.setName(createRandomLetterString(MAX_LENGTH_256 + 1));

        actorRoleRepository.save(actorRole1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testActorRoleIsNotSavedWithEmptyName() {
        actorRole1.setName("");

        actorRoleRepository.save(actorRole1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testActorRoleIsNotSavedWithNullName() {
        actorRole1.setName(null);

        actorRoleRepository.save(actorRole1);
    }

    @Rollback
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testActorRoleIsSavedWithUniqueName() {
        actorRoleRepository.save(actorRole1);

        String name = actorRole1.getName();
        actorRole2.setName(name);

        actorRoleRepository.save(actorRole2);
    }
}