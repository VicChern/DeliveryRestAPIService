package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.TenantPropertiesRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.utils.ITTestUtils;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;


//TODO: Use SpringDBUnit for DB setup before test class
//https://springtestdbunit.github.io/spring-test-dbunit/sample.html

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantPropertiesTestIT extends AbstractTestNGSpringContextTests {

    public static final int MAX_KEY_LENGTH = 256;
    public static final int MAX_VALUE_LENGTH = 4096;
    @Autowired
    private TenantPropertiesRepository repository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserRepository userRepository;
    private TenantProperties tenantProperties1;
    private TenantProperties tenantProperties2;

    @BeforeClass
    public void setUp() {
        tenantProperties1 = ITTestUtils
                .getTenantProperties(getTransientFieldsForTenantProperies());
        tenantProperties2 = ITTestUtils
                .getTenantProperties(getTransientFieldsForTenantProperies());
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveProperty_keyIsNull_ExceptionThrown() {
        tenantProperties1.setKey(null);
        //when
        repository.save(tenantProperties1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveProperty_keyLongerThan256_ExceptionThrown() {
        String generatedString = RandomString.make(MAX_KEY_LENGTH + 1);
        tenantProperties1.setKey(generatedString);
        //when
        repository.save(tenantProperties1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveProperty_valueLongerThan4096_ExceptionThrown() {
        String generatedString = RandomString.make(MAX_VALUE_LENGTH + 1);
        tenantProperties1.setValue(generatedString);
        //when
        repository.save(tenantProperties1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveProperty_ValueIsNull_ExceptionThrown() {
        tenantProperties1.setValue(null);
        //when
        repository.save(tenantProperties1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveProperty_KeyIsDuplicated_ExceptionThrown() {
        //given
        repository.save(tenantProperties1);
        String tenantProperties1Key = tenantProperties1.getKey();
        tenantProperties2.setKey(tenantProperties1Key);
        repository.save(tenantProperties1);
        //when
        repository.save(tenantProperties2);
    }

    private Tenant getTransientFieldsForTenantProperies() {
        User user = ITTestUtils.getUser();
        Tenant tenant = ITTestUtils.getTenant(user);
        userRepository.save(user);
        tenantRepository.save(tenant);
        return tenant;
    }
}
