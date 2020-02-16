package com.softserve.itacademy.kek.models;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantProperties;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.TenantPropertiesRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getPropertyType;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getTenantProperties;
import static org.testng.Assert.assertNotNull;


@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantPropertiesTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private TenantPropertiesRepository tenantPropertiesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;

    private TenantProperties tenantProperties;
    private User user1;
    private User user2;
    private Tenant tenant1;
    private Tenant tenant2;

    @DataProvider(name = "illegal_keys")
    public static Object[][] keys() {
        return new Object[][]{{createRandomLetterString(MAX_LENGTH_256 + 1)}, {""}, {null}};
    }

    @DataProvider(name = "illegal_values")
    public static Object[][] values() {
        return new Object[][]{{createRandomLetterString(MAX_LENGTH_4096 + 1)}, {""}, {null}};
    }

    @BeforeMethod
    public void setUp() {

        user1 = createOrdinaryUser(1);
        tenant1 = createOrdinaryTenant(1);

        userRepository.save(user1);
        assertNotNull(userRepository.findById(user1.getIdUser()));

        tenant1.setTenantOwner(user1);
        tenantRepository.save(tenant1);
        assertNotNull(tenantRepository.findById(tenant1.getIdTenant()));

        tenantProperties = getTenantProperties(tenant1, getPropertyType());

    }

    @AfterMethod
    public void tearDown() {
        tenantPropertiesRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test(groups = {"integration-tests"})
    public void testTenantPropertiesIsSavedWithValidFields() {
        //when
        tenantPropertiesRepository.save(tenantProperties);

        Optional<TenantProperties> savedTenantProperty = tenantPropertiesRepository.findById(tenantProperties.getIdProperty());
        //then
        Assert.assertNotNull(savedTenantProperty.orElse(null));
        Assert.assertEquals(savedTenantProperty.get().getIdProperty(), tenantProperties.getIdProperty());
    }

    @Test(groups = {"integration-tests"}, dataProvider = "illegal_keys", expectedExceptions =
            {ConstraintViolationException.class, DataIntegrityViolationException.class})
    public void testTenantPropertiesIsNotSavedWithIllegalKey(String key) {
        tenantProperties.setKey(key);
        //when
        tenantPropertiesRepository.save(tenantProperties);
    }

    @Test(groups = {"integration-tests"}, dataProvider = "illegal_values", expectedExceptions =
            {ConstraintViolationException.class, DataIntegrityViolationException.class})
    public void testTenantPropertiesIsNotSavedWithIllegalValue(String value) {
        tenantProperties.setValue(value);
        //when
        tenantPropertiesRepository.save(tenantProperties);
    }

    @Test(groups = {"integration-tests"}, expectedExceptions = DataIntegrityViolationException.class)
    public void testTenantPropertiesIsSavedWithUniqueKey() {
        //given
        user2 = createOrdinaryUser(2);
        tenant2 = createOrdinaryTenant(2);

        userRepository.save(user2);
        assertNotNull(userRepository.findById(user2.getIdUser()));

        tenant2.setTenantOwner(user2);
        tenantRepository.save(tenant2);
        assertNotNull(tenantRepository.findById(tenant2.getIdTenant()));

        tenantPropertiesRepository.save(tenantProperties);
        assertNotNull(tenantPropertiesRepository.findById(tenantProperties.getIdProperty()));

        TenantProperties tenantProperties2 = getTenantProperties(tenant2, getPropertyType());
        tenantProperties2.setKey(tenantProperties.getKey());

        //when
        tenantPropertiesRepository.save(tenantProperties2);
    }

}
