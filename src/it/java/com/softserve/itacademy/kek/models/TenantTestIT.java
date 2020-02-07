package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.Random;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static org.testng.Assert.assertNotNull;

/**
 * Integration tests for {@link Tenant} constraints.
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantTestIT extends AbstractTestNGSpringContextTests {

    private User user;
    private Tenant tenant;

    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeMethod
    void setUp() {
        user = createOrdinaryUser(1);
        tenant = createOrdinaryTenant(1);

        userRepository.save(user);
        assertNotNull(userRepository.findById(user.getIdUser()));

        tenant.setTenantOwner(user);
    }

    @AfterMethod
    void tearDown() {
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test(description = "Test Tenant_01. Should save tenant with valid fields for existing in db user.")
    public void testTenantIsSavedWithValidFields() {
        //when
        tenantRepository.save(tenant);
    }

    //====================================================== guid ======================================================
    @Test(description = "Test Tenant_02. Should throw ConstraintViolationException when saves tenant with null guid field for existing in db user",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testTenantIsNotSavedWithNullGuid() {
        //given
        tenant.setGuid(null);

        //when
        tenantRepository.save(tenant);
    }

    @Test(description = "Test Tenant_03. Should throw DataIntegrityViolationException when saves tenant with not unique guid field for existing in db user",
            expectedExceptions = DataIntegrityViolationException.class,
            expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void testTenantIsSavedWithUniqueGuid() {
        //given
        User user1 = user;

        //   save second user to db
        User user2 = createOrdinaryUser(2);
        userRepository.save(user2);
        assertNotNull(userRepository.findById(user2.getIdUser()));

        //  prepare two tenants with equals guid
        Tenant tenant1 = tenant;
        Tenant tenant2 = createOrdinaryTenant(2);

        tenant1.setGuid(tenant2.getGuid());

        tenant1.setTenantOwner(user1);
        tenant2.setTenantOwner(user2);

        //when
        tenantRepository.save(tenant1);
        assertNotNull(tenantRepository.findById(tenant1.getIdTenant()).orElse(null));
        tenantRepository.save(tenant2);
    }


    //====================================================== name ======================================================

    @Test(description = "Test Tenant_04. Should throw ConstraintViolationException when saves tenant with null name field for existing in db user",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testTenantIsNotSavedWithNullName() {
        //given
        tenant.setName(null);

        //when
        tenantRepository.save(tenant);
    }

    @Test(description = "Test Tenant_05. Should throw ConstraintViolationException when saves tenant with empty name field for existing in db user",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testTenantIsNotSavedWithEmptyName() {
        //given
        tenant.setName("");

        //when
        tenantRepository.save(tenant);
    }

    @Test(description = "Test Tenant_06. Should throw ConstraintViolationException when saves tenant (for existing in db user) with name field length more than " + MAX_LENGTH_256,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testTenantIsNotSavedWithNameMoreThanMaxLength() {
        //given
        String name = createRandomLetterString(MAX_LENGTH_256 + 1 + new Random().nextInt(50));
        tenant.setName(name);

        //when
        tenantRepository.save(tenant);
    }

}
