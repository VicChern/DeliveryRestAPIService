package com.softserve.itacademy.kek.utils;

import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantProperties;
import com.softserve.itacademy.kek.models.User;
import net.bytebuddy.utility.RandomString;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ITTestUtils {

    //ToDo: change for builders
    //ToDo: extract values into constants

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
        user.setPhoneNumber("380-50-444-55-" + getRandomIntegerInRange(10, 99));
        user.setEmail(RandomString.make() + "@gmail.com");
        return user;
    }

    private static int getRandomIntegerInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static TenantProperties getTenantProperties(Tenant tenant) {
        TenantProperties properties = new TenantProperties();
        properties.setKey(RandomString.make());
        properties.setValue(RandomString.make());
        properties.setTenant(tenant);
        return properties;
    }

}
