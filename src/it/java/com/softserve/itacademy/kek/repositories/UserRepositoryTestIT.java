package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

//TODO: Add Logger
//TODO: Run Tests with integration.properties
@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class UserRepositoryTestIT extends AbstractTestNGSpringContextTests {

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
        user.setGuid(UUID.fromString("60ceb31a-f709-4049-93f9-ce59946abc1f"));
        user.setNickname("Joss");
        user.setPhoneNumber("123-234-345-5");
        user.setEmail("segreg@gmail.com");
        return user;
    }


    @Test
    public void whenNameSizeMoreThan256() {
        User user = nameMaxSize();

        //Проверка на то, что лежит в user.name
    }

    private User nameMaxSize() {
        User user = new User();

        //Добавление в user.name строки длиннок > 256

        return user;
    }
}