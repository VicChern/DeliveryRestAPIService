package com.softserve.itacademy.kek.utils;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import net.bytebuddy.utility.RandomString;

import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Address;
import com.softserve.itacademy.kek.models.impl.GlobalProperties;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.IdentityType;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantDetails;
import com.softserve.itacademy.kek.models.impl.TenantProperties;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;

/**
 * Util class for creating entities in integration tests
 */
public class ITCreateEntitiesUtils {

    public static final int MAX_LENGTH_256 = 256;
    public static final int MAX_LENGTH_512 = 512;
    public static final int MAX_LENGTH_1024 = 1024;
    public static final int MAX_LENGTH_4096 = 4096;

    public static final String PHONE_NUMBER_PART = "380-50-444-55-";
    public static final String GMAIL_COM = "@gmail.com";


    //================================================== User entity ===================================================

    /**
     * Creates {@link User} with simple fields (guid, name, nickname, email, phoneNumber) depending on {@param i}
     *
     * @param i
     * @return user
     */
    public static User createOrdinaryUser(int i) {
        return createSimpleUser(
                UUID.randomUUID(),
                "name" + i,
                "nickname" + i,
                "email" + i + GMAIL_COM,
                PHONE_NUMBER_PART + 55 + i);
    }

    /**
     * Creates {@link User} with simple fields (guid, name, nickname, email, phoneNumber) and empty {@link UserDetails}
     *
     * @param guid
     * @param name
     * @param nickName
     * @param email
     * @param phoneNumber
     * @return user
     */
    public static User createSimpleUser(UUID guid, String name, String nickName, String email, String phoneNumber) {
        User user = new User();
        user.setName(name);
        user.setGuid(guid);
        user.setNickname(nickName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);

        UserDetails userDetails = new UserDetails();

        user.setUserDetails(userDetails);
        userDetails.setUser(user);

        return user;
    }

    /**
     * Creates {@link UserDetails} with valid fields, but without {@link User}
     *
     * @return userDetails
     */
    public static UserDetails createSimpleUserDetailsWithValidFields() {
        UserDetails userDetails = new UserDetails();
        userDetails.setPayload(createRandomLetterString(500));
        userDetails.setImageUrl(createRandomLetterString(60));
        return userDetails;
    }

    //================================================ Identity entity =================================================

    /**
     * Creates {@link Identity} with valid fields
     *
     * @return identity
     */
    public static Identity createIdentity() {
        Identity identity = new Identity();
        identity.setPayload(createRandomLetterString(500));
        identity.setIdentityType(createIdentityType());
        return identity;
    }

    /**
     * Creates {@link IdentityType} with valid fields
     *
     * @return identityType
     */
    public static IdentityType createIdentityType() {
        IdentityType identityType = new IdentityType();
        identityType.setName(createRandomLetterString(60));
        return identityType;
    }

    //================================================== Tenant entity =================================================

    /**
     * Creates {@link Tenant} with simple fields (guid, name) depending on {@param i}
     *
     * @param i
     * @return tenant
     */
    public static Tenant createOrdinaryTenant(int i) {
        return createSimpleTenant(
                UUID.randomUUID(),
                "name" + i
        );
    }

    /**
     * Creates {@link Tenant} with simple fields (guid, name) and empty {@link TenantDetails}
     *
     * @param guid
     * @param name
     * @return tenant
     */
    public static Tenant createSimpleTenant(UUID guid, String name) {
        Tenant tenant = new Tenant();
        tenant.setName(name);
        tenant.setGuid(guid);

        TenantDetails tenantDetails = new TenantDetails();

        tenant.setTenantDetails(tenantDetails);
        tenantDetails.setTenant(tenant);

        return tenant;
    }

    public static Tenant getTenant(User user) {
        Tenant tenant = new Tenant();
        tenant.setGuid(UUID.randomUUID());
        tenant.setName(randomString());
        tenant.setTenantOwner(user);

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setTenant(tenant);
        tenant.setTenantDetails(tenantDetails);
        return tenant;
    }

    public static TenantProperties getTenantProperties(Tenant tenant, PropertyType type) {
        TenantProperties properties = new TenantProperties();
        properties.setGuid(UUID.randomUUID());
        properties.setKey(createRandomLetterString(64));
        properties.setValue(createRandomLetterString(256));
        properties.setTenant(tenant);
        properties.setPropertyType(type);
        return properties;
    }

    /**
     * Creates {@link TenantDetails} with valid field, but without {@link Tenant}
     *
     * @return tenantDetails
     */
    public static TenantDetails createSimpleTenantDetailsWithValidFields() {
        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setPayload(createRandomLetterString(500));
        tenantDetails.setImageUrl(createRandomLetterString(60));
        return tenantDetails;
    }

    //================================================= Address entity =================================================

    /**
     * Creates {@link Address} with simple fields (guid, alias, address, notes)
     *
     * @param guid
     * @param alias
     * @param addressValue
     * @param notes
     * @return address
     */
    public static Address createSimpleAddress(UUID guid, String alias, String addressValue, String notes) {
        Address address = new Address();
        address.setGuid(guid);
        address.setAlias(alias);
        address.setAddress(addressValue);
        address.setNotes(notes);
        return address;
    }

    /**
     * Creates {@link Address} with simple fields (guid, alias, address, notes) depending on {@param i}
     *
     * @param i
     * @return address
     */
    public static Address createOrdinaryAddress(int i) {
        return createSimpleAddress(
                UUID.randomUUID(),
                "alias" + i,
                "addressValue" + i,
                "notes" + i);
    }


    //============================================== GlobalProperty entity =============================================
    public static GlobalProperties getGlobalProperty(PropertyType type) {
        GlobalProperties properties = new GlobalProperties();
        properties.setPropertyType(type);
        properties.setKey(randomString());
        properties.setValue(randomString());
        return properties;
    }

    public static PropertyType getPropertyType() {
        PropertyType propertyType = new PropertyType();
        propertyType.setName(createRandomLetterString(64));
        propertyType.setSchema(createRandomLetterString(64));
        return propertyType;
    }


    //================================================== Order entity ==================================================
    public static Order getOrder(Tenant tenant) {
        Order order = new Order();

        order.setTenant(tenant);
        order.setGuid(UUID.randomUUID());
        order.setSummary(createRandomLetterString(128));

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setImageUrl(createRandomLetterString(128));
        orderDetails.setPayload(createRandomLetterString(128));
        order.setOrderDetails(orderDetails);
        orderDetails.setOrder(order);
        return order;
    }

    public static OrderEventType getOrderEventType() {
        OrderEventType orderEventType = new OrderEventType();

        orderEventType.setName(createRandomLetterString(128));

        return orderEventType;
    }

    public static OrderEvent getOrderEvent(Order order, OrderEventType orderEventType, Actor actor) {
        OrderEvent orderEvent = new OrderEvent();

        orderEvent.setOrder(order);
        orderEvent.setActor(actor);
        orderEvent.setOrderEventType(orderEventType);
        orderEvent.setGuid(UUID.randomUUID());
        orderEvent.setPayload(createRandomLetterString(MAX_LENGTH_512));

        return orderEvent;
    }

    public static OrderDetails getOrderDetails(Order order) {
        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setOrder(order);
        orderDetails.setPayload(createRandomLetterString(2048));
        orderDetails.setImageUrl(createRandomLetterString(MAX_LENGTH_256));

        return orderDetails;
    }

    //================================================== Actor entity ==================================================
    public static Actor getActor(User user, Tenant tenant) {
        Actor actor = new Actor();

        actor.setTenant(tenant);
        actor.setUser(user);
        actor.setGuid(UUID.randomUUID());
        actor.setAlias(createRandomLetterString(128));

        return actor;
    }

    public static Actor getActor(User user, Tenant tenant, ActorRole actorRole) {
        Actor actor = new Actor();

        actor.setTenant(tenant);
        actor.setUser(user);
        actor.setGuid(UUID.randomUUID());
        actor.setAlias(createRandomLetterString(128));
        actor.getActorRoles().add(actorRole);

        return actor;
    }

    public static ActorRole getActorRole(String name) {
        ActorRole actorRole = new ActorRole();
        actorRole.setName(name);
        return actorRole;
    }


    //================================================== common methods ==================================================

    /**
     * Creates random {@link String} with chars from 'a' to 'z' and from 'A' to 'Z' according to ASCII table
     *
     * @param stringLength length of random string
     * @return random string
     * @see <a href="http://www.asciitable.com">http://www.asciitable.com</a>
     */
    public static String createRandomLetterString(int stringLength) {
        return RandomString.make(stringLength);
    }

    public static String randomString() {
        return createRandomLetterString(8);
    }

    /**
     * Creates random {@link String} with chars from '0' to '9' according to ASCII table
     *
     * @param stringLength length of random string
     * @return random string
     * @see <a href="http://www.asciitable.com">http://www.asciitable.com</a>
     */
    public static String createRandomNumberString(int stringLength) {
        char[] stringNumberSymbols;
        StringBuilder sb = new StringBuilder();
        for (char character = '0'; character <= '9'; character++) {
            sb.append(character);
        }
        stringNumberSymbols = sb.toString().toCharArray();

        Random random = new Random();
        char[] buffer = new char[stringLength];
        for (int index = 0; index < stringLength; index++) {
            buffer[index] = stringNumberSymbols[random.nextInt(stringNumberSymbols.length)];
        }
        return new String(buffer);
    }

    private static int getRandomIntegerInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
