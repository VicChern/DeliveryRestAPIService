package com.softserve.itacademy.kek.controller.utils;

public class KekRoles {
    public final static String TENANT_USER_ADMIN = "hasRole('TENANT') or hasRole('USER') or hasRole ('ADMIN')";
    public final static String USER_ADMIN = "hasRole('USER') or hasRole ('ADMIN')";
    public final static String TENANT_ADMIN = "hasRole('TENANT') or hasRole('ADMIN')";
    public final static String ADMIN = "hasRole('ADMIN')";
    public final static String ANY_ROLE = "hasRole('TENANT') or hasRole('USER') or hasRole('ACTOR') or hasRole ('ADMIN')";
}
