package com.softserve.itacademy.kek;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = {"integration-tests"})
public class KekMainTestIT {

    @BeforeMethod
    public void setUp() {
        System.out.println("KekMainTestIT - setUp - ok");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("KekMainTestIT - tearDown - ok");
    }

    @Test
    public void testMain() {
        System.out.println("KekMainTestIT - testMain - ok");
    }
}