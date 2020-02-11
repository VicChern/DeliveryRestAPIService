package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.TenantPropertiesRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getTenantProperties;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getUser;


@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantPropertiesTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private TenantPropertiesRepository tenantPropertiesRepository;
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @DataProvider(name="illegal_keys")
    public static Object[][] keys(){
        return new Object[][]{{createRandomLetterString(MAX_LENGTH_256 + 1)}, {""}, {null}};
    }

    @DataProvider(name="illegal_values")
    public static Object[][] values(){
        return new Object[][]{{createRandomLetterString(MAX_LENGTH_4096 + 1)}, {""}, {null}};
    }

    @BeforeMethod
    public void setUp() {
        user1 = getUserWithTenantProperties();
        user2 = getUserWithTenantProperties();
    }

    @AfterMethod
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test(dataProvider = "illegal_keys", expectedExceptions =
            {ConstraintViolationException.class, DataIntegrityViolationException.class})
    public void testTenantPropertiesIsNotSavedWithIllegalKey(String key) {
        user1.getTenant().getTenantPropertiesList().get(0).setKey(key);
        //when
        userRepository.save(user1);
    }

    @Test(dataProvider = "illegal_values", expectedExceptions =
            {ConstraintViolationException.class, DataIntegrityViolationException.class})
    public void testTenantPropertiesIsNotSavedWithIllegalValue(String value) {
        user1.getTenant().getTenantPropertiesList().get(0).setValue(value);
        //when
        userRepository.save(user1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testTenantPropertiesIsSavedWithUniqueKey() {
        //given
        userRepository.save(user1);
        String tenantProperties1Key = user1.getTenant().getTenantPropertiesList().get(0).getKey();
        user2.getTenant().getTenantPropertiesList().get(0).setKey(tenantProperties1Key);
        //when
        userRepository.save(user2);
    }

    @Test
    public void testTenantPropertiesIsSavedWithValidFields() {
        userRepository.save(user1);
        Long id = user1.getTenant().getTenantPropertiesList().get(0).getIdProperty();
        //when
        Optional<TenantProperties> savedTenantProperty = tenantPropertiesRepository.findById(id);
        //then
        Assert.assertNotNull(savedTenantProperty.orElse(null));
        Assert.assertEquals(savedTenantProperty.get().getIdProperty(), id);
    }

//TODO: Extract into utils, refactor to Builder, refactor to User with all transitive dependencies
    private User getUserWithTenantProperties() {
        User user = getUser();
        Tenant tenant = getTenant(user);
        user.setTenant(tenant);
        PropertyType propertyType = ITCreateEntitiesUtils.getPropertyType();
        TenantProperties tenantProperties = getTenantProperties(tenant, propertyType);
        ArrayList<TenantProperties> propertiesList = new ArrayList<>(
                List.of(tenantProperties)
        );
        tenant.setTenantPropertiesList(propertiesList);
        return user;
    }

}
