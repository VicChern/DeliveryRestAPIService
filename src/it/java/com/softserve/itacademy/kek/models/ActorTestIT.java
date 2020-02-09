package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.utils.ITTestUtils;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

@Rollback
@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class ActorTestIT extends AbstractTestNGSpringContextTests {

    public static final int MAX_ALIAS_LENGTH = 256;

    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActorRepository actorRepository;

    private Actor actor1;
    private Actor actor;

    @BeforeClass
    void getActor() {
        actor = ITTestUtils.getActor(getUserForActor(), getTenantForActor());
    }

    @BeforeMethod
    public void setUp() {
        actor1 = actor;
    }

    @Rollback
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenGUIDIsNotUnique() {
        actorRepository.save(actor1);

        UUID guid = actor1.getGuid();

        Actor actor2 = ITTestUtils.getActor(getUserForActor(), getTenantForActor());
        actor2.setGuid(guid);

        actorRepository.save(actor2);
    }

    @Rollback
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenGUIDIsNull() {
        Actor actor2 = ITTestUtils.getActor(getUserForActor(), getTenantForActor());

        actor2.setGuid(null);

        actorRepository.save(actor2);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenAliasSizeMoreThan256() {
        Actor actor2 = ITTestUtils.getActor(getUserForActor(), getTenantForActor());

        actor2.setAlias(RandomString.make(MAX_ALIAS_LENGTH + 1));

        actorRepository.save(actor2);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenAliasSizeLessThan1() {
        Actor actor2 = ITTestUtils.getActor(getUserForActor(), getTenantForActor());

        actor2.setAlias("");

        actorRepository.save(actor2);
    }

    @Rollback
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenAliasIsNull() {
        Actor actor2 = ITTestUtils.getActor(getUserForActor(), getTenantForActor());

        actor2.setAlias(null);

        actorRepository.save(actor2);
    }

    private Tenant getTenantForActor() {
        Tenant tenant = ITTestUtils.getTenant(getUserForActor());

        tenantRepository.save(tenant);

        return tenant;
    }

    private User getUserForActor() {
        User user = ITTestUtils.getUser();

        userRepository.save(user);

        return user;
    }
}