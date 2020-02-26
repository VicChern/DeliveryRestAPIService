package com.softserve.itacademy.kek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantServiceTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private ITenantService tenantService;
    @Autowired
    private IUserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;

    private IUser user;
    private Tenant tenant;

    @BeforeMethod
    public void setUp() {

        user = createOrdinaryUser(1);
        tenant = createOrdinaryTenant(1);

        IUser savedUser = userService.create(user);
        assertNotNull(savedUser);

        tenant.setTenantOwner(savedUser);
    }

    @AfterMethod
    public void tearDown() {
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createSuccess() {
        //when
        ITenant savedTenant = tenantService.create(tenant);

        //then
        assertNotNull(savedTenant);
        ITenant foundTenant = tenantRepository.findByGuid(savedTenant.getGuid()).orElse(null);
        assertNotNull(foundTenant);
        assertEquals(foundTenant, savedTenant);
    }

    @Test
    public void getAllSuccess() {
        //given
        ITenant savedTenant = tenantService.create(tenant);

        //when
        List<ITenant> tenants = tenantService.getAll();

        assertEquals(tenants.size(), 1);
        assertEquals(tenants.get(0), savedTenant);
    }
}
