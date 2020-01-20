package com.softserve.itacademy.kek;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = {"unit-tests"})
public class KekMainTest {

    @BeforeMethod
    public void setUp() {
        System.out.println("KekMainTest - setUp - ok");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("KekMainTest - tearDown - ok");
    }

    @Test
    public void testMain() {
        System.out.println("KekMainTest - testMain - ok");
    }
}