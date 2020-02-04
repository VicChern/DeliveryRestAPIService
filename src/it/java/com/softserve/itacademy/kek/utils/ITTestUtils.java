package com.softserve.itacademy.kek.utils;

import com.softserve.itacademy.kek.models.PropertyType;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantProperties;
import com.softserve.itacademy.kek.models.User;
import net.bytebuddy.utility.RandomString;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ITTestUtils {
    public static final String PHONE_NUMBER_PART = "380-50-444-55-";
    public static final String GMAIL_COM = "@gmail.com";

    public static Tenant getTenant(User user) {
        Tenant tenant = new Tenant();
        tenant.setGuid(UUID.randomUUID());
        tenant.setName(RandomString.make());
        tenant.setTenantOwner(user);
        return tenant;
    }

    public static User getUser() {
        User user = new User();
        user.setName(RandomString.make());
        user.setGuid(UUID.randomUUID());
        user.setNickname(RandomString.make());
        user.setPhoneNumber(PHONE_NUMBER_PART
                .concat(String.valueOf(getRandomIntegerInRange(10, 99))));
        user.setEmail(RandomString.make() + GMAIL_COM);
        return user;
    }

    public static TenantProperties getTenantProperties(Tenant tenant) {
        TenantProperties properties = new TenantProperties();
        properties.setKey(RandomString.make());
        properties.setValue(RandomString.make());
        properties.setTenant(tenant);
        return properties;
    }

    private static int getRandomIntegerInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static PropertyType getPropertyType() {
        PropertyType propertyType = new PropertyType();
        propertyType.setName(RandomString.make());
        propertyType.setSchema(RandomString.make());
        return propertyType;
    }
}
