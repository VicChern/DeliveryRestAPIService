package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;

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

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {
        actorRole1 = getActorRole("actorRole1");
        actorRole2 = getActorRole("actorRole2");
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        actorRoleRepository.deleteAll();
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testActorRoleIsNotSavedWithNameMoreThanMaxLength() {
        actorRole1.setName(createRandomLetterString(MAX_LENGTH_256 + 1));

        actorRoleRepository.save(actorRole1);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testActorRoleIsNotSavedWithEmptyName() {
        actorRole1.setName("");

        actorRoleRepository.save(actorRole1);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testActorRoleIsNotSavedWithNullName() {
        actorRole1.setName(null);

        actorRoleRepository.save(actorRole1);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = DataIntegrityViolationException.class)
    public void testActorRoleIsSavedWithUniqueName() {
        actorRoleRepository.save(actorRole1);

        String name = actorRole1.getName();
        actorRole2.setName(name);

        actorRoleRepository.save(actorRole2);
    }
}