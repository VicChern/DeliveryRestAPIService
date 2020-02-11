package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.modelInterfaces.ITenant;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.impl.TenantServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit tests for {@link TenantServiceImpl}
 */
public class TenantServiceTest {

    private User user;
    private ITenant tenant;

    @InjectMocks
    private TenantServiceImpl tenantService;
    @Mock
    private TenantRepository tenantRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user = createOrdinaryUser(1);
        tenant = createOrdinaryTenant(1);

        tenant.setTenantOwner(user);
    }

    @AfterMethod
    void tearDown() {
        verifyNoMoreInteractions(tenantRepository, userRepository);
    }


    //  ============================================== save(ITenant tenant)() ==============================================
    @Test
    void saveSuccess() {
        //given
        when(tenantRepository.save((Tenant) tenant)).thenReturn((Tenant)tenant);
        when(userRepository.findByGuid(any(UUID.class))).thenReturn(user);

        // when
        ITenant createdTenant = tenantService.save(tenant);

        // then
        assertNotNull(createdTenant);
        assertEquals(createdTenant.getName(), tenant.getName());
        assertNotNull(createdTenant.getGuid());
        assertEquals(createdTenant.getTenantDetails().getPayload(), tenant.getTenantDetails().getPayload());
        assertEquals(createdTenant.getTenantDetails().getImageUrl(), tenant.getTenantDetails().getImageUrl());

        verify(tenantRepository, times(1)).save((Tenant) tenant);
        verify(userRepository, times(1)).findByGuid(tenant.getTenantOwner().getGuid());
    }


}
