package com.softserve.itacademy.kek.controller.utils;

/**
 * Constraints of Json Media Types for:
 * {@link com.softserve.itacademy.kek.controller.TenantController}
 * {@link com.softserve.itacademy.kek.controller.OrderController}
 * {@link com.softserve.itacademy.kek.controller.UserController}
 * {@link com.softserve.itacademy.kek.controller.SignInController}
 * {@link com.softserve.itacademy.kek.controller.AuthController}
 * {@link com.softserve.itacademy.kek.controller.RegistrationController}
 */
public class KekMediaType {

    public static final String SOFTSERVE = "application/vnd.softserve";
    public static final String TENANT = SOFTSERVE + ".tenant+json";
    public static final String TENANT_LIST = SOFTSERVE + ".tenantList+json";
    public static final String TENANT_PROPERTY = SOFTSERVE + ".tenantproperty+json";
    public static final String TENANT_PROPERTY_LIST = SOFTSERVE + ".tenantPropertyList+json";
    public static final String ADDRESS = SOFTSERVE + ".address+json";
    public static final String ADDRESS_LIST = SOFTSERVE + ".addressList+json";
    public static final String USER = SOFTSERVE + ".user+json";
    public static final String USER_LIST = SOFTSERVE + ".userList+json";
    public static final String ORDER = SOFTSERVE + ".order+json";
    public static final String ORDER_LIST = SOFTSERVE + ".orderList+json";
    public static final String EVENT = SOFTSERVE + ".event+json";
    public static final String EVENT_LIST = SOFTSERVE + ".eventList+json";
    public static final String REGISTRATION_USER = SOFTSERVE + ".registrationUser+json";
    public static final String SIGNIN = SOFTSERVE + ".signin+json";
    public static final String ACTOR_LIST = SOFTSERVE + ".actorList+json";
    public static final String TOKEN = SOFTSERVE + ".token+json";

}

