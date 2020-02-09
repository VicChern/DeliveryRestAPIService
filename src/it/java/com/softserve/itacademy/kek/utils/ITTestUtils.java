package com.softserve.itacademy.kek.utils;

import com.softserve.itacademy.kek.models.Actor;
import com.softserve.itacademy.kek.models.ActorRole;
import com.softserve.itacademy.kek.models.GlobalProperties;
import com.softserve.itacademy.kek.models.Order;
import com.softserve.itacademy.kek.models.OrderDetails;
import com.softserve.itacademy.kek.models.OrderEvent;
import com.softserve.itacademy.kek.models.OrderEventType;
import com.softserve.itacademy.kek.models.PropertyType;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantDetails;
import com.softserve.itacademy.kek.models.TenantProperties;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.models.UserDetails;
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
        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setTenant(tenant);
        tenant.setTenantDetails(tenantDetails);
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
        UserDetails userDetails = new UserDetails();
        userDetails.setUser(user);
        user.setUserDetails(userDetails);
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

    public static GlobalProperties getGlobalProperty(PropertyType type) {
        GlobalProperties properties = new GlobalProperties();
        properties.setPropertyType(type);
        properties.setKey(RandomString.make());
        properties.setValue(RandomString.make());
        return properties;
    }

    public static Actor getActor(User user, Tenant tenant) {
        Actor actor = new Actor();

        actor.setIdTenant(tenant);
        actor.setIdUser(user);
        actor.setGuid(UUID.randomUUID());
        actor.setAlias(RandomString.make(128));

        return actor;
    }

    public static Order getOrder(Tenant tenant) {
        Order order = new Order();

        order.setIdOrder(0L);
        order.setIdTenant(tenant);
        order.setGuid(UUID.randomUUID());
        order.setSummary(RandomString.make(128));

        return order;
    }

    public static ActorRole getActorRole() {
        ActorRole actorRole = new ActorRole();

        actorRole.setName(RandomString.make(128));

        return actorRole;
    }

    public static OrderEventType getOrderEventType() {
        OrderEventType orderEventType = new OrderEventType();

        orderEventType.setName(RandomString.make(128));

        return orderEventType;
    }

    public static OrderEvent getOrderEvent(Order order, Actor actor, OrderEventType orderEventType) {
        OrderEvent orderEvent = new OrderEvent();

        orderEvent.setIdOrder(order);
        orderEvent.setIdActor(actor);
        orderEvent.setIdOrderEventType(orderEventType);
        orderEvent.setGuid(UUID.randomUUID());
        orderEvent.setPayload(RandomString.make(512));

        return orderEvent;
    }

    public static OrderDetails getOrderDetails(Order order) {
        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setOrder(order);
        orderDetails.setPayload(RandomString.make(2048));
        orderDetails.setImageUrl(RandomString.make(256));

        return orderDetails;
    }
}
