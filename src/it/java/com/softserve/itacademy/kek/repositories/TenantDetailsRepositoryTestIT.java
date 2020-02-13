package com.softserve.itacademy.kek.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantDetails;
import com.softserve.itacademy.kek.models.User;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantDetailsRepositoryTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private TenantDetailsRepository repository;

    @Test
    public void whenSaveThenAdded() {
        List<TenantDetails> tenantDetailsList = new ArrayList<>();
        tenantDetailsList.add(instantiateTenantDetails());

        repository.saveAll(tenantDetailsList);
        Iterable<TenantDetails> all = repository.findAll();
        long count = StreamSupport.stream(all.spliterator(), false)
                .map(TenantDetails::getPayload)
                .count();

        Assert.assertEquals(count, 1);
    }


    private TenantDetails instantiateTenantDetails() {
        TenantDetails tenantDetails = new TenantDetails();

        Tenant tenant = new Tenant();

        User user = new User();
        user.setName("Test");
        user.setGuid(UUID.fromString("60ceb31a-f709-4049-93f9-ce59646abc1f"));
        user.setNickname("Joss");
        user.setPhoneNumber("123-234-345-5");
        user.setEmail("segreg@gmail.com");

        tenant.setGuid(UUID.fromString("60ceb31a-f709-4049-93f9-ce59946abc1f"));
        tenant.setTenantOwner(user);
        tenant.setName("MyName");

        tenantDetails.setTenant(tenant);
        tenantDetails.setPayload("payload");
        tenantDetails.setImageUrl("url");

        return tenantDetails;
    }
}
