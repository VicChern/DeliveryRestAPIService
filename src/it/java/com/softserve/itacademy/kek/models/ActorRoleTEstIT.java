package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class ActorRoleTEstIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private ActorRole actorRole;

    @Test
    public void whenActorRoleIsOk() {

    }

    @Test
    public void whenNameSizeMoreThan256() {

    }

    @Test
    public void whenNameSizeLessThan1() {

    }

    @Test
    public void whenNameIsNull() {

    }

    @Test
    public void whenNameSizeIsOk() {

    }

    @Test
    public void whenNameIsNotUnique() {

    }

    @Test
    public void whenNameIsUnique() {

    }
}
