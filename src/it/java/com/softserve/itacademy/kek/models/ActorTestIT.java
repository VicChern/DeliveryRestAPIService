package com.softserve.itacademy.kek.models;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

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

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getActor;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getActorRole;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getTenant;


@Rollback
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class ActorTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private ActorRoleRepository actorRoleRepository;

    private ActorRole actorRole;
    private Actor actor1;
    private Actor actor2;


    @BeforeMethod
    public void setUp() {
        actorRole = getActorRole("actorRole");
        actorRoleRepository.save(actorRole);

        Tenant tenant1 = getTenantForActor(1);
        actor1= getActor((User)tenant1.getTenantOwner(), tenant1, actorRole);
    }

    @AfterMethod
    public void tearDown() {
        actorRepository.deleteAll();
        actorRoleRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Rollback
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testActorIsSavedWithUniqueGuid() {
        actorRepository.save(actor1);

        Tenant tenant2 = getTenantForActor(2);
        actor2 = getActor((User)tenant2.getTenantOwner(), tenant2);

        UUID guid = actor1.getGuid();
        actor2.setGuid(guid);

        actorRepository.save(actor2);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testActorIsNotSavedWithNullGuid() {
        actor1.setGuid(null);

        actorRepository.save(actor1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testActorIsNotSavedWithAliasMoreThanMaxLength() {
        actor1.setAlias(createRandomLetterString(MAX_LENGTH_256 + 1));

        actorRepository.save(actor1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testActorIsNotSavedWithEmptyAlias() {
        actor1.setAlias("");

        actorRepository.save(actor1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testActorIsNotSavedWithNullAlias() {
        actor1.setAlias(null);

        actorRepository.save(actor1);
    }

    private Tenant getTenantForActor(int i) {
        Tenant tenant = getTenant(getUserForActor(i));

        tenantRepository.save(tenant);

        return tenant;
    }

    private User getUserForActor(int i) {
        User user = createOrdinaryUser(i);

        userRepository.save(user);

        return user;
    }
}