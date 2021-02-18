package com.vicchern.deliveryservice.service;

import com.vicchern.deliveryservice.configuration.PersistenceTestConfig;
import com.vicchern.deliveryservice.models.enums.ActorRoleEnum;
import com.vicchern.deliveryservice.models.impl.Actor;
import com.vicchern.deliveryservice.models.impl.ActorRole;
import com.vicchern.deliveryservice.models.impl.Tenant;
import com.vicchern.deliveryservice.models.impl.User;
import com.vicchern.deliveryservice.repositories.ActorRepository;
import com.vicchern.deliveryservice.repositories.ActorRoleRepository;
import com.vicchern.deliveryservice.repositories.TenantRepository;
import com.vicchern.deliveryservice.repositories.UserRepository;
import com.vicchern.deliveryservice.services.IActorService;
import com.vicchern.deliveryservice.utils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

        user = ITCreateEntitiesUtils.createOrdinaryUser(1);
        customer = ITCreateEntitiesUtils.createOrdinaryUser(2);
        tenant = ITCreateEntitiesUtils.createOrdinaryTenant(1);

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
        Actor createActor = (Actor) actorService.create(tenant, user, ActorRoleEnum.CUSTOMER);

        //then
        assertNotNull(createActor);
        assertEquals(createActor.getUser().getGuid(), user.getGuid());
        assertEquals(createActor.getTenant().getGuid(), tenant.getGuid());
    }

}
