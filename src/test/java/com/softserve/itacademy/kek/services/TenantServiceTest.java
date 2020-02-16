package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.exception.ServiceException;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantDetails;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.modelInterfaces.ITenant;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.impl.TenantServiceImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.UUID;

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
        tenantService  = new TenantServiceImpl(tenantRepository, userRepository);

        user = createOrdinaryUser(1);
        tenant = createOrdinaryTenant(1);
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
        when(userRepository.findByGuid(any(UUID.class))).thenReturn((User) user);
        when(tenantRepository.save(any(Tenant.class))).thenReturn((Tenant) tenant);

        // when
        ITenant createdTenant = tenantService.create(tenant);

        // then
        assertNotNull(createdTenant);
        assertEquals(createdTenant.getName(), tenant.getName());
        assertNotNull(createdTenant.getGuid());
        assertEquals(createdTenant.getTenantDetails().getPayload(), tenant.getTenantDetails().getPayload());
        assertEquals(createdTenant.getTenantDetails().getImageUrl(), tenant.getTenantDetails().getImageUrl());

        verify(userRepository, times(1)).findByGuid(any(UUID.class));
        verify(tenantRepository, times(1)).save(any(Tenant.class));

    }

    @Test(expectedExceptions = ServiceException.class)
    void createThrowsServiceExceptionWhenRepositoryThrowsEntityNotFoundException() {
        //given
        when(userRepository.findByGuid(any(UUID.class))).thenThrow(EntityNotFoundException.class);

        // when
        ITenant save = tenantService.create(tenant);
    }

    @Test(expectedExceptions = ServiceException.class)
    void createThrowsServiceExceptionWhenRepositoryThrowsPersistenceException() {
        //given
        when(userRepository.findByGuid(any(UUID.class))).thenReturn((User)user);
        when(tenantRepository.save(any(Tenant.class))).thenThrow(PersistenceException.class);

        // when
        tenantService.create(tenant);
    }

    //  ============================================= getByGuid(UUID guid) =============================================
    @Test
    void getByGuidSuccess() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn((Tenant)tenant);

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
    void getByGuidThrowsServiceExceptionWhenRepositoryThrowsEntityNotFoundException() {
       //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn(null);

        // when
        tenantService.getByGuid(tenant.getGuid());
    }


    //  ====================================== update(ITenant tenant, UUID guid) =======================================
    @Test
    void updateSuccess() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn((Tenant)tenant);
        when(tenantRepository.save(any(Tenant.class))).thenReturn((Tenant) tenant);

        Tenant updatedTenant = (Tenant)tenant;
        updatedTenant.setName("newName");
        updatedTenant.setTenantDetails(new TenantDetails());

        // when
        ITenant createdTenant = tenantService.update(updatedTenant, user.getGuid());

        // then
        assertNotNull(createdTenant);
        assertEquals(createdTenant.getName(), tenant.getName());
        assertNotNull(createdTenant.getGuid());
        assertEquals(createdTenant.getTenantDetails().getPayload(), tenant.getTenantDetails().getPayload());
        assertEquals(createdTenant.getTenantDetails().getImageUrl(), tenant.getTenantDetails().getImageUrl());

        verify(tenantRepository, times(1)).findByGuid(any(UUID.class));
        verify(tenantRepository, times(1)).save(any(Tenant.class));
    }

    @Test(expectedExceptions = ServiceException.class)
    void updateThrowsServiceExceptionWhenRepositoryThrowsEntityNotFoundException() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn(null);

        // when
        tenantService.update(tenant, user.getGuid());
    }

    @Test(expectedExceptions = ServiceException.class)
    void updateThrowsServiceExceptionWhenRepositoryThrowsPersistenceException() {
        //given
        when(tenantRepository.findByGuid(any(UUID.class))).thenReturn((Tenant) tenant);
        when(tenantRepository.save(any(Tenant.class))).thenThrow(PersistenceException.class);

        // when
        tenantService.update(tenant, user.getGuid());
    }

    //  =========================================== deleteByGuid(UUID guid) ============================================
    @Test
    void deleteByGuidSuccess() {
        //given
        doNothing().when(tenantRepository).removeByGuid(any(UUID.class));

        // when
        tenantService.deleteByGuid(tenant.getGuid());

        verify(tenantRepository, times(1)).removeByGuid(any(UUID.class));
    }

    @Test(expectedExceptions = ServiceException.class)
    void deleteThrowsServiceExceptionWhenRepositoryThrowsEntityNotFoundException() {
        //given
        doThrow(EmptyResultDataAccessException.class).when(tenantRepository).removeByGuid(any(UUID.class));

        // when
        tenantService.deleteByGuid(tenant.getGuid());
    }
}
