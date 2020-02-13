package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.TenantDetailsRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.TransactionSystemException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.Random;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_512;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createSimpleTenantDetailsWithValidFields;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Integration tests for {@link TenantDetails} constraints.
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantDetailsTestIT extends AbstractTestNGSpringContextTests {
    private User user;
    private Tenant tenant;
    private TenantDetails tenantDetails;

    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private TenantDetailsRepository tenantDetailsRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeMethod
    void setUp() {
        user = createOrdinaryUser(1);
        tenant = createOrdinaryTenant(1);
        tenantDetails = createSimpleTenantDetailsWithValidFields();

        //given
        userRepository.save(user);
        assertNotNull(userRepository.findById(user.getIdUser()));

        tenant.setTenantDetails(tenantDetails);
        tenantDetails.setTenant(tenant);
        tenant.setTenantOwner(user);
    }

    @AfterMethod
    void tearDown() {
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test(description = "Test TenantDetails_01. Should save tenant with valid tenantDetails fields for existing in db user.")
    public void testTenantIsSavedWithValidFields() {
        //when
        tenantRepository.save(tenant);

        //then
        assertEquals(1, tenantRepository.findAll().spliterator().estimateSize());
        Optional<Tenant> tenantOptional = tenantRepository.findById(tenant.getIdTenant());
        assertNotNull(tenantOptional.orElse(null));

        //delete if we wouldn't use tenantDetailsRepository
        assertEquals(1, tenantDetailsRepository.findAll().spliterator().estimateSize());
        Optional<TenantDetails> tenantDetailsOptional = tenantDetailsRepository.findById(tenantDetails.getIdTenant());
        assertNotNull(tenantDetailsOptional.orElse(null));

        TenantDetails tenantDetails = (TenantDetails) tenantOptional.get().getTenantDetails();
        assertEquals(tenantOptional.get().getIdTenant(), tenantDetails.getIdTenant());
    }


    //==================================================== payload =====================================================

    @Test(description = "Test TenantDetails_02. Should throw TransactionSystemException when saves tenant with a payload field length more than " + MAX_LENGTH_4096,
            expectedExceptions = TransactionSystemException.class,
            expectedExceptionsMessageRegExp = "Could not commit JPA transaction; .*")
    public void testTenantDetailsIsNotSavedWithPayloadMoreThanMaxLength() {
        //given
        String payload = createRandomLetterString(MAX_LENGTH_4096 + 1 + new Random().nextInt(50));
        tenantDetails.setPayload(payload);

        //when
        tenantRepository.save(tenant);
    }


    //==================================================== imageUrl ====================================================
    @Test(description = "Test TenantDetails_03. Should throw TransactionSystemException when saves tenant with a imageUrl field length more than " + MAX_LENGTH_512,
            expectedExceptions = TransactionSystemException.class,
            expectedExceptionsMessageRegExp = "Could not commit JPA transaction; .*")
    public void testTenantDetailsIsNotSavedWithImageUrlMoreThanMaxLength() {
        //given
        String imageUrl = createRandomLetterString(MAX_LENGTH_512 + 1 + new Random().nextInt(50));
        tenantDetails.setImageUrl(imageUrl);

        //when
        tenantRepository.save(tenant);
    }
}
