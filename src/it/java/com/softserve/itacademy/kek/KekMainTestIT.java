package com.softserve.itacademy.kek;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class KekMainTestIT {

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {
        System.out.println("KekMainTestIT - setUp - ok");
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        System.out.println("KekMainTestIT - tearDown - ok");
    }

    @Test(groups = {"integration-tests"})
    public void testMain() {
        System.out.println("KekMainTestIT - testMain - ok");
    }
}