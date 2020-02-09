package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.TenantPropertiesRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.utils.ITTestUtils;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;


@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantPropertiesTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private TenantPropertiesRepository tenantPropertiesRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserRepository userRepository;

    private TenantProperties tenantProperties1;
    private TenantProperties tenantProperties2;

    @BeforeMethod
    public void setUp() {
        tenantProperties1 = ITTestUtils
                .getTenantProperties(getTransientFieldsForTenantProperies());
        tenantProperties2 = ITTestUtils
                .getTenantProperties(getTransientFieldsForTenantProperies());
    }

    @AfterMethod
    public void tearDown() {
        deleteTransientFieldsForTenantProperties();
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void saveProperty_keyIsNull_ExceptionThrown() {
        tenantProperties1.setKey(null);
        //when
        tenantPropertiesRepository.save(tenantProperties1);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void saveProperty_keyLongerThan256_ExceptionThrown() {
        String generatedString = RandomString.make(MAX_LENGTH_256 + 1);
        tenantProperties1.setKey(generatedString);
        //when
        tenantPropertiesRepository.save(tenantProperties1);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void saveProperty_valueLongerThan4096_ExceptionThrown() {
        String generatedString = RandomString.make(MAX_LENGTH_4096 + 1);
        tenantProperties1.setValue(generatedString);
        //when
        tenantPropertiesRepository.save(tenantProperties1);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void saveProperty_ValueIsNull_ExceptionThrown() {
        tenantProperties1.setValue(null);
        //when
        tenantPropertiesRepository.save(tenantProperties1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveProperty_KeyIsDuplicated_ExceptionThrown() {
        //given
        tenantPropertiesRepository.save(tenantProperties1);
        String tenantProperties1Key = tenantProperties1.getKey();
        tenantProperties2.setKey(tenantProperties1Key);
        tenantPropertiesRepository.save(tenantProperties1);
        //when
        tenantPropertiesRepository.save(tenantProperties2);
    }

    @Test
    public void saveProperty_findById_returnProperty() {
        tenantPropertiesRepository.save(tenantProperties1);
        Long id = tenantProperties1.getIdProperty();
        //when
        Optional<TenantProperties> savedTenantProperty = tenantPropertiesRepository.findById(id);
        //then
        Assert.assertNotNull(savedTenantProperty.orElse(null));
        Assert.assertEquals(savedTenantProperty.get().getIdProperty(), id);
    }

    private Tenant getTransientFieldsForTenantProperies() {
        User user = ITTestUtils.getUser();
        Tenant tenant = ITTestUtils.getTenant(user);
        userRepository.save(user);
        tenantRepository.save(tenant);
        return tenant;
    }

    private void deleteTransientFieldsForTenantProperties() {
        userRepository.deleteAll();
        tenantRepository.deleteAll();
        tenantPropertiesRepository.deleteAll();
    }
}
