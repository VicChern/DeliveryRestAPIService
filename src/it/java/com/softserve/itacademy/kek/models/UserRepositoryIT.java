package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceJPAConfig;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

// TODO: Add logger
@ContextConfiguration(classes = {PersistenceJPAConfig.class})
public class UserRepositoryIT extends AbstractTestNGSpringContextTests{

    @Autowired
    private UserRepository repository;

    //TODO Change this test to check constraint or add asserts, extract user creating into beforeTest or in
    // separate method in utility class
    //Temporary test created only to check basic workflow
    @Test
    public void whenSaveThenAdded() {
        List<User> users = new ArrayList<>();
        User usr1 = new User();
        usr1.setName("Test");
        usr1.setGuid("60ceb31a-f709-4049-93f9-ce59946abc1f");
        usr1.setNickname("Joss");
        usr1.setPhoneNumber("123-234-345-5");
        usr1.setEmail("segreg@gmail.com");
        users.add(usr1);

        repository.saveAll(users);

        repository.saveAll(users);

        Iterable<User> all = repository.findAll();

        long count = StreamSupport.stream(all.spliterator(), false)
                .map(User::getName)
                .count();

        System.out.println(count);
    }

}