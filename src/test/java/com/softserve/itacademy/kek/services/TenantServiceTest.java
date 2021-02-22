package com.softserve.itacademy.kek.services;

import java.util.Optional;
import java.util.UUID;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.exception.ServiceException;
import com.softserve.itacademy.kek.exception.TenantServiceException;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantDetails;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.impl.TenantServiceImpl;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit tests for {@link TenantServiceImpl}
 */
@Test(groups = {"unit-tests"})
public class TenantServiceTest {

    private User user;
    private Tenant tenant;

    private TenantServiceImpl tenantService;
    private TenantRepository tenantRepository;
    private UserRepository userRepository;

    @BeforeClass
    void setClassUp() {
    }

    @BeforeMethod
    void setUp() {
        tenantRepository = mock(TenantRepository.class);
        userRepository = mock(UserRepository.class);
        tenantService = new TenantServiceImpl(tenantRepository, userRepository);

        user = createOrdinaryUser(1);
        user.setIdUser(1L);

        tenant = createOrdinaryTenant(1);
        tenant.setIdTenant(1L);
        tenant.setTenantOwner(user);
    }

    @AfterMethod
    void tearDown() {
        reset(tenantRepository);
        reset(userRepository);
    }


    //  ============================================ create(ITenant tenant)() ============================================
    @Test
    void createSuccess() {
        //given
        when(userRepository.findByGuid(any(UUID.class))).thenReturn(Optional.ofNullable(user));
        when(tenantRepository.saveAndFlush(any(Tenant.class))).thenReturn(tenant);

        // when
        ITenant createdTenant = tenantService.create(tenant);

        // then
        assertNotNull(createdTenant);
        assertEquals(createdTenant.getName(), tenant.getName());
        assertNotNull(createdTenant.getGuid());
        assertEquals(createdTenant.getTenantDetails().getPayload(), tenant.getTenantDetails().getPayload());
        assertEquals(createdTenant.getTenantDetails().getImageUrl(), tenant.getTenantDetails().getImageUrl());

        verify(userRepository, times(1)).findByGuid(any(UUID.class));
        verify(tenantRepository, times(1)).saveAndFlush(any(Tenant.class));

    }

    @Test(expectedExceptions = ServiceException.class)
    void createThrowsServiceExceptionWhenRepositoryReturnsEmptyTenantOwner() {
        //given
        when(userRepository.findByGuid(any(UUID.class))).thenReturn(Optional.empty());

        // when
        ITenant save = tenantService.create(tenant);
    }

    @Test(expectedExceptions = ServiceException.class)
    void createThrowsServiceExceptionWhenRepositoryThrowsPersistenceException() {
        //given
        when(userRepository.findByGuid(any(UUID.class))).thenReturn(Optional.ofNullable(user));
        when(tenantRepository.saveAndFlush(any(Tenant.class))).thenThrow(TenantServiceException.class);

        // when
        tenantService.create(tenant);
    }

    //  ============================================= getByGuid(UUID guid) =============================================
    @Test
    void getByGuidSuccess() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn(Optional.ofNullable(tenant));

        // when
        ITenant gettedTenant = tenantService.getByGuid(tenant.getGuid());

        // then
        assertNotNull(gettedTenant);
        assertEquals(gettedTenant.getName(), tenant.getName());
        assertNotNull(gettedTenant.getGuid());
        assertEquals(gettedTenant.getTenantDetails().getPayload(), tenant.getTenantDetails().getPayload());
        assertEquals(gettedTenant.getTenantDetails().getImageUrl(), tenant.getTenantDetails().getImageUrl());

        verify(tenantRepository, times(1)).findByGuid(any(UUID.class));
    }

    @Test(expectedExceptions = ServiceException.class)
    void getByGuidThrowsServiceExceptionWhenRepositoryReturnsEmptyOptional() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn(Optional.empty());

        // when
        tenantService.getByGuid(tenant.getGuid());
    }


    //  ====================================== update(ITenant tenant, UUID guid) =======================================
    @Test
    void updateSuccess() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn(Optional.ofNullable(tenant));

        Tenant updatedTenant = tenant;
        updatedTenant.setName("newName");
        updatedTenant.setTenantDetails(new TenantDetails());

        when(tenantRepository.saveAndFlush(any(Tenant.class))).thenReturn(tenant);

        // when
        ITenant createdTenant = tenantService.update(updatedTenant, user.getGuid());

        // then
        assertNotNull(createdTenant);
        assertEquals(createdTenant.getName(), updatedTenant.getName());
        assertNotNull(createdTenant.getGuid());
        assertEquals(createdTenant.getTenantDetails().getPayload(), updatedTenant.getTenantDetails().getPayload());
        assertEquals(createdTenant.getTenantDetails().getImageUrl(), updatedTenant.getTenantDetails().getImageUrl());

        verify(tenantRepository, times(1)).findByGuid(any(UUID.class));
        verify(tenantRepository, times(1)).saveAndFlush(any(Tenant.class));
    }

    @Test(expectedExceptions = ServiceException.class)
    void updateThrowsServiceExceptionWhenRepositoryReturnsEmptyOptional() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn(Optional.empty());

        // when
        tenantService.update(tenant, user.getGuid());
    }

    @Test(expectedExceptions = ServiceException.class)
    void updateThrowsServiceExceptionWhenRepositoryThrowsPersistenceException() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn(Optional.ofNullable(tenant));
        when(tenantRepository.saveAndFlush(any(Tenant.class))).thenThrow(TenantServiceException.class);

        // when
        tenantService.update(tenant, user.getGuid());
    }

    //  =========================================== deleteByGuid(UUID guid) ============================================
    @Test
    void deleteByGuidSuccess() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn(Optional.ofNullable(tenant));
        doNothing().when(tenantRepository).deleteById(any(Long.class));
        doNothing().when(tenantRepository).flush();

        // when
        tenantService.deleteByGuid(tenant.getGuid());

        verify(tenantRepository, times(1)).deleteById(any(Long.class));
    }

    @Test(expectedExceptions = ServiceException.class)
    void deleteThrowsServiceExceptionWhenRepositoryReturnsEmptyOptional() {
        //given
        doThrow(TenantServiceException.class).when(tenantRepository).findByGuid(any(UUID.class));

        // when
        tenantService.deleteByGuid(tenant.getGuid());
    }
}
