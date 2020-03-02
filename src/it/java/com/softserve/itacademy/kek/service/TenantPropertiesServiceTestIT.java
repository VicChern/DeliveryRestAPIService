package com.softserve.itacademy.kek.service;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.ITenantProperties;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantProperties;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.TenantPropertiesRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.ITenantPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getPropertyType;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getTenantProperties;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantPropertiesServiceTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private ITenantPropertiesService tenantPropertiesService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private TenantPropertiesRepository tenantPropertiesRepository;

    private User user;
    private Tenant tenant;
    private TenantProperties tenantProperty;
    private List<ITenantProperties> tenantProperties;

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {
        user = createOrdinaryUser(1);
        tenant = createOrdinaryTenant(1);

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);

        tenant.setTenantOwner(savedUser);
        Tenant savedTenant = tenantRepository.save(tenant);
        assertNotNull(savedTenant);

        tenantProperties = new ArrayList<>();
        tenantProperties.add(getTenantProperties(tenant, getPropertyType()));
        tenantProperties.add(getTenantProperties(tenant, getPropertyType()));
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        tenantPropertiesRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    // ======================= create(List<ITenantProperties> iTenantProperties, UUID tenantGuid) ======================
    @Rollback
    @Test(groups = {"integration-tests"})
    public void createSuccess() {
        //when
        List<ITenantProperties> savedTenantProperties = tenantPropertiesService.create(tenantProperties, tenant.getGuid());

        //then
        assertNotNull(savedTenantProperties);
        assertEquals(savedTenantProperties.size(), tenantProperties.size());

        Set<String> keys = savedTenantProperties
                .stream()
                .map(ITenantProperties::getKey)
                .collect(Collectors.toSet());
        Set<String> keys1 = tenantProperties
                .stream()
                .map(ITenantProperties::getKey)
                .collect(Collectors.toSet());
        assertTrue(keys.containsAll(keys1));
    }

    // =======================================  getAllForTenant(UUID tenantGuid) =======================================
    @Rollback
    @Test(groups = {"integration-tests"})
    public void getAllForTenantSuccess() {
        //given
        tenantProperties.forEach(tenantProperty -> tenant.addTenantProperty((TenantProperties) tenantProperty));
        tenant = tenantRepository.save(tenant);
        List<TenantProperties> savedTenantProperties = tenant.getTenantPropertiesList();
        assertNotNull(savedTenantProperties);

        //when
        List<ITenantProperties> receivedTenantProperties = tenantPropertiesService.getAllForTenant(tenant.getGuid());

        //then
        assertNotNull(receivedTenantProperties);
        assertEquals(savedTenantProperties.size(), receivedTenantProperties.size());

        Set<String> keys = savedTenantProperties
                .stream()
                .map(ITenantProperties::getKey)
                .collect(Collectors.toSet());
        Set<String> keys1 = receivedTenantProperties
                .stream()
                .map(ITenantProperties::getKey)
                .collect(Collectors.toSet());
        assertTrue(keys.containsAll(keys1));
    }

    // ================================== get(UUID tenantGuid, UUID tenantPropertyGuid) ================================
    @Rollback
    @Test(groups = {"integration-tests"})
    public void getSuccess() {
        //given
        tenantProperties.forEach(tenantProperty -> tenant.addTenantProperty((TenantProperties) tenantProperty));
        tenant = tenantRepository.save(tenant);
        List<TenantProperties> savedTenantProperties = tenant.getTenantPropertiesList();
        assertNotNull(savedTenantProperties);

        //when
        ITenantProperties receivedTenantProperty = tenantPropertiesService.get(
                tenant.getGuid(),
                savedTenantProperties.get(0).getGuid());

        //then
        assertNotNull(receivedTenantProperty);

        assertEquals(receivedTenantProperty, savedTenantProperties.get(0));
    }


    // =============== update(UUID tenantGuid, UUID tenantPropertyGuid, ITenantProperties tenantProperty)===============
    @Rollback
    @Test(groups = {"integration-tests"})
    public void updateSuccess() {
        //given
        tenantProperties.forEach(tenantProperty -> tenant.addTenantProperty((TenantProperties) tenantProperty));
        tenant = tenantRepository.save(tenant);
        List<TenantProperties> savedTenantProperties = tenant.getTenantPropertiesList();
        assertNotNull(savedTenantProperties);

        TenantProperties propertyForUpdate = new TenantProperties();
        propertyForUpdate.setKey("updated key");
        propertyForUpdate.setValue("updated value");

        PropertyType propertyTypeForUpdate = new PropertyType();
        propertyTypeForUpdate.setName("updated name");
        propertyTypeForUpdate.setSchema("updated schema");

        propertyForUpdate.setPropertyType(propertyTypeForUpdate);

        //when
        ITenantProperties updatedTenantProperty = tenantPropertiesService.update(
                tenant.getGuid(),
                savedTenantProperties.get(0).getGuid(),
                propertyForUpdate);

        //then
        assertNotNull(updatedTenantProperty);
        assertEquals(propertyForUpdate.getKey(), updatedTenantProperty.getKey());
        assertEquals(propertyForUpdate.getValue(), updatedTenantProperty.getValue());
        assertEquals(propertyForUpdate.getPropertyType().getName(), updatedTenantProperty.getPropertyType().getName());
        assertEquals(propertyForUpdate.getPropertyType().getSchema(), updatedTenantProperty.getPropertyType().getSchema());
    }


    // ================================ delete(UUID tenantGuid, UUID tenantPropertyGuid) ===============================
    @Rollback
    @Test(groups = {"integration-tests"})
    public void deleteSuccess() {
        //given
        tenantProperties.forEach(tenantProperty -> tenant.addTenantProperty((TenantProperties) tenantProperty));
        tenant = tenantRepository.save(tenant);
        List<TenantProperties> savedTenantProperties = tenant.getTenantPropertiesList();
        assertNotNull(savedTenantProperties);

        //when
        tenantPropertiesService.delete(tenant.getGuid(), savedTenantProperties.get(0).getGuid());

        //then
        assertFalse(tenantPropertiesRepository.findByGuidAndTenantGuid(savedTenantProperties.get(0).getGuid(), tenant.getGuid()).isPresent());
    }
}
