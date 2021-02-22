package com.vicchern.deliveryservice.models;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

import com.vicchern.deliveryservice.configuration.PersistenceTestConfig;
import com.vicchern.deliveryservice.models.impl.Actor;
import com.vicchern.deliveryservice.models.impl.ActorRole;
import com.vicchern.deliveryservice.models.impl.Tenant;
import com.vicchern.deliveryservice.models.impl.User;
import com.vicchern.deliveryservice.repositories.ActorRepository;
import com.vicchern.deliveryservice.repositories.ActorRoleRepository;
import com.vicchern.deliveryservice.repositories.TenantRepository;
import com.vicchern.deliveryservice.repositories.UserRepository;
import com.vicchern.deliveryservice.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


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


    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {
        actorRole = ITCreateEntitiesUtils.getActorRole("actorRole");
        actorRoleRepository.save(actorRole);

        Tenant tenant1 = getTenantForActor(1);
        actor1 = ITCreateEntitiesUtils.getActor((User) tenant1.getTenantOwner(), tenant1, actorRole);
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        actorRepository.deleteAll();
        actorRoleRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = DataIntegrityViolationException.class)
    public void testActorIsSavedWithUniqueGuid() {
        actorRepository.save(actor1);

        Tenant tenant2 = getTenantForActor(2);
        actor2 = ITCreateEntitiesUtils.getActor((User) tenant2.getTenantOwner(), tenant2);

        UUID guid = actor1.getGuid();
        actor2.setGuid(guid);

        actorRepository.save(actor2);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testActorIsNotSavedWithNullGuid() {
        actor1.setGuid(null);

        actorRepository.save(actor1);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testActorIsNotSavedWithAliasMoreThanMaxLength() {
        actor1.setAlias(ITCreateEntitiesUtils.createRandomLetterString(ITCreateEntitiesUtils.MAX_LENGTH_256 + 1));

        actorRepository.save(actor1);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testActorIsNotSavedWithEmptyAlias() {
        actor1.setAlias("");

        actorRepository.save(actor1);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testActorIsNotSavedWithNullAlias() {
        actor1.setAlias(null);

        actorRepository.save(actor1);
    }

    private Tenant getTenantForActor(int i) {
        Tenant tenant = ITCreateEntitiesUtils.getTenant(getUserForActor(i));

        tenantRepository.save(tenant);

        return tenant;
    }

    private User getUserForActor(int i) {
        User user = ITCreateEntitiesUtils.createOrdinaryUser(i);

        userRepository.save(user);

        return user;
    }
}