package com.softserve.itacademy.kek.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;


//TODO: Add Logger
@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserRepositoryTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository repository;

}