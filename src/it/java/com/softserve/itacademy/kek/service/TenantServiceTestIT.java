package com.softserve.itacademy.kek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {

        user = createOrdinaryUser(1);
        tenant = createOrdinaryTenant(1);

        final IUser savedUser = userService.create(user);
        assertNotNull(savedUser);

        tenant.setTenantOwner(savedUser);
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test(groups = {"integration-tests"})
    public void createSuccess() {
        //when
        final ITenant savedTenant = tenantService.create(tenant);

        //then
        assertNotNull(savedTenant);
        final ITenant foundTenant = tenantRepository.findByGuid(savedTenant.getGuid()).orElse(null);
        assertNotNull(foundTenant);
        assertEquals(foundTenant, savedTenant);
    }

    @Test(groups = {"integration-tests"})
    public void getAllSuccess() {
        //given
        final ITenant savedTenant = tenantService.create(tenant);

        //when
        final List<ITenant> tenants = tenantService.getAll();

        //then
        assertEquals(tenants.size(), 1);
        assertEquals(tenants.get(0), savedTenant);
    }

    @Test(groups = {"integration-tests"})
    public void getAllPageableSuccess() {
        //given
        final ITenant savedTenant = tenantService.create(tenant);
        final Pageable pageable = PageRequest.of(0, 1);

        //when
        final Page<ITenant> tenants = tenantService.getAllPageable(pageable);

        //then
        assertEquals(tenants.getTotalPages(), 1);
        assertEquals(tenants.getContent().get(0), savedTenant);
    }

}
