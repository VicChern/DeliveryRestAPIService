package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;


//TODO: Add Logger
@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserRepositoryTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository repository;

}