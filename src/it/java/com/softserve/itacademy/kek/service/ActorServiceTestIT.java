package com.softserve.itacademy.kek.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IActorService;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class ActorServiceTestIT extends AbstractTestNGSpringContextTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private ActorRoleRepository actorRoleRepository;

    @Autowired
    IActorService actorService;

    private Actor actor;
    private ActorRole actorRole1;
    private ActorRole actorRole2;

    private User user;
    private User customer;
    private Tenant tenant;

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {
        actor = new Actor();
        actor.setUser(user);
        actor.setTenant(tenant);

        actorRole1 = new ActorRole();
        actorRole1.setName(ActorRoleEnum.CUSTOMER.toString());
        actorRole2 = new ActorRole();
        actorRole2.setName(ActorRoleEnum.CURRIER.toString());

        actorRoleRepository.save(actorRole1);
        actorRoleRepository.save(actorRole2);

        user = createOrdinaryUser(1);
        customer = createOrdinaryUser(2);
        tenant = createOrdinaryTenant(1);

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);

        User savedCustomer = userRepository.save(customer);
        assertNotNull(savedCustomer);

        tenant.setTenantOwner(savedUser);
        Tenant savedTenant = tenantRepository.save(tenant);
        assertNotNull(savedTenant);
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        actorRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
        actorRoleRepository.deleteAll();
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void createSuccess() {
        //when
        Actor createActor = actorService.saveActor(tenant, user, actorRole1);

        //then
        assertNotNull(createActor);
        assertEquals(createActor.getUser().getGuid(), user.getGuid());
        assertEquals(createActor.getTenant().getGuid(), tenant.getGuid());
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void getAllByUserSuccess() {
        //given
        Actor createActor = actorService.saveActor(tenant, user, actorRole1);

        //when
        List<Optional<Actor>> actorList = new ArrayList<>();
        actorList.add(actorRepository.findByUserGuid(createActor.getUser().getGuid()));

        //then
        assertNotNull(createActor);
        assertNotNull(actorList);
        assertEquals(actorList.size(), 1);
    }
}
