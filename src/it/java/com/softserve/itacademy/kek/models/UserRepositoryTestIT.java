package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceJPAConfig;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

//TODO: Add Logger
//TODO: Run Tests with integration.properties
@ContextConfiguration(classes = {PersistenceJPAConfig.class})
public class UserRepositoryTestIT extends AbstractTestNGSpringContextTests{

    @Autowired
    private UserRepository repository;

    //TODO Change this test to check constraint or add asserts, extract user creating into beforeTest or in
    // separate method in utility class
    //Temporary test created only to check basic workflow
    @Test
    public void whenSaveThenAdded() {
        List<User> users = new ArrayList<>();
        users.add(instantiateUser());

        repository.saveAll(users);
        Iterable<User> all = repository.findAll();
        long count = StreamSupport.stream(all.spliterator(), false)
                .map(User::getName)
                .count();

        Assert.assertEquals(count, 1);
    }


    private User instantiateUser() {
        User user = new User();
        user.setName("Test");
        user.setGuid("60ceb31a-f709-4049-93f9-ce59946abc1f");
        user.setNickname("Joss");
        user.setPhoneNumber("123-234-345-5");
        user.setEmail("segreg@gmail.com");
        return user;
    }

}