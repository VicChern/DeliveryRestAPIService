package com.vicchern.deliveryservice.models;


import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Random;

import com.vicchern.deliveryservice.configuration.PersistenceTestConfig;
import com.vicchern.deliveryservice.models.impl.Address;
import com.vicchern.deliveryservice.models.impl.Tenant;
import com.vicchern.deliveryservice.models.impl.User;
import com.vicchern.deliveryservice.repositories.AddressRepository;
import com.vicchern.deliveryservice.repositories.TenantRepository;
import com.vicchern.deliveryservice.repositories.UserRepository;
import com.vicchern.deliveryservice.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Integration tests for {@link Address} constraints.
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class AddressTestIT extends AbstractTestNGSpringContextTests {

    private Address address;
    private User user;
    private Tenant tenant;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;


    @BeforeMethod(groups = {"integration-tests"})
    void setUp() {

        address = ITCreateEntitiesUtils.createOrdinaryAddress(1);
        user = ITCreateEntitiesUtils.createOrdinaryUser(1);
        tenant = ITCreateEntitiesUtils.createOrdinaryTenant(1);

        //given
        userRepository.save(user);
        assertNotNull(userRepository.findById(user.getIdUser()));

        tenant.setTenantOwner(user);
        tenantRepository.save(tenant);
        assertNotNull(tenantRepository.findById(tenant.getIdTenant()));

        address.setUser(user);
        address.setTenant(tenant);
    }

    @AfterMethod(groups = {"integration-tests"})
    void tearDown() {
        addressRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test(groups = {"integration-tests"},
            description = "Test Address_01. Should save address with valid fields.")
    public void testTenantIsSavedWithValidFields() {
        //when
        addressRepository.save(address);

        //then
        assertEquals(1, addressRepository.findAll().spliterator().estimateSize());

        Optional<Address> addressOptional = addressRepository.findById(address.getIdAddress());
        assertNotNull(addressOptional.orElse(null));

        assertEquals(user, addressOptional.get().getUser());
        assertEquals(tenant, addressOptional.get().getTenant());
    }

    //====================================================== guid ======================================================
    @Test(groups = {"integration-tests"},
            description = "Test Address_02. Should throw ConstraintViolationException when saves address with null guid field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testAddressIsNotSavedWithNullGuid() {
        //given
        address.setGuid(null);

        //when
        addressRepository.save(address);
    }

    @Test(groups = {"integration-tests"},
            description = "Test Address_03. Should throw DataIntegrityViolationException when saves address with not unique guid field",
            expectedExceptions = DataIntegrityViolationException.class,
            expectedExceptionsMessageRegExp = "could not execute statement; .*")
    public void testAddressIsSavedWithUniqueGuid() {
        //given
        Address address1 = address;
        Address address2 = ITCreateEntitiesUtils.createOrdinaryAddress(2);

        address2.setGuid(address1.getGuid());
        assertEquals(address1.getGuid(), address2.getGuid());

        //when
        Address savedAddress1 = addressRepository.save(address1);
        assertEquals(savedAddress1, address1);
        addressRepository.save(address2);
    }


    //====================================================== address ======================================================
    @Test(groups = {"integration-tests"},
            description = "Test Address_04. Should throw ConstraintViolationException when saves address with null address field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testAddressIsNotSavedWithNullAddressValue() {
        //given
        address.setAddress(null);

        //when
        addressRepository.save(address);
    }

    @Test(groups = {"integration-tests"},
            description = "Test Address_05. Should throw ConstraintViolationException when saves address with empty address field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testAddressIsNotSavedWithEmptyAddressValue() {
        //given
        address.setAddress("");

        //when
        addressRepository.save(address);
    }

    @Test(groups = {"integration-tests"},
            description = "Test Address_06. Should throw ConstraintViolationException when saves address with address field length more than " + ITCreateEntitiesUtils.MAX_LENGTH_512,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testAddressIsNotSavedWithAddressValueMoreThanMaxLength() {
        //given
        String addressValue = ITCreateEntitiesUtils.createRandomLetterString(ITCreateEntitiesUtils.MAX_LENGTH_512 + 1 + new Random().nextInt(50));
        address.setAddress(addressValue);

        //when
        addressRepository.save(address);
    }


    //====================================================== notes ======================================================
    @Test(groups = {"integration-tests"},
            description = "Test Address_07. Should throw ConstraintViolationException when saves address with notes field length more than " + ITCreateEntitiesUtils.MAX_LENGTH_512,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testAddressIsNotSavedWithNotesMoreThanMaxLength() {
        //given
        String notes = ITCreateEntitiesUtils.createRandomLetterString(ITCreateEntitiesUtils.MAX_LENGTH_1024 + 1 + new Random().nextInt(50));
        address.setAddress(notes);

        //when
        addressRepository.save(address);
    }


    //====================================================== alias ======================================================
    @Test(groups = {"integration-tests"},
            description = "Test Address_08. Should throw ConstraintViolationException when saves address with null alias field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testAddressIsNotSavedWithNullAlias() {
        //given
        address.setAlias(null);

        //when
        addressRepository.save(address);
    }

    @Test(groups = {"integration-tests"},
            description = "Test Address_09. Should throw ConstraintViolationException when saves address with empty alias field",
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testAddressIsNotSavedWithEmptyAlias() {
        //given
        address.setAlias("");

        //when
        addressRepository.save(address);
    }

    @Test(groups = {"integration-tests"},
            description = "Test Address_10. Should throw ConstraintViolationException when saves address with alias field length more than " + ITCreateEntitiesUtils.MAX_LENGTH_256,
            expectedExceptions = ConstraintViolationException.class,
            expectedExceptionsMessageRegExp = "Validation failed .*")
    public void testAddressIsNotSavedWithAliasMoreThanMaxLength() {
        //given
        String alias = ITCreateEntitiesUtils.createRandomLetterString(ITCreateEntitiesUtils.MAX_LENGTH_256 + 1 + new Random().nextInt(50));
        address.setAlias(alias);

        //when
        addressRepository.save(address);
    }

}
