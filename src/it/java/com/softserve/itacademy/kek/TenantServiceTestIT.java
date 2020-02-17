package com.softserve.itacademy.kek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;

@Test(groups = {"integration-tests"})
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class TenantServiceTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private ITenantService tenantService;
    @Autowired
    private IUserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @BeforeMethod
    public void setUp() {
        userRepository.deleteAll();
    }

    @AfterMethod
    public void tearDown() {
//        userRepository.deleteAll();
    }

    @Rollback
    @Test
    public void createSuccess() {
       IUser user = createOrdinaryUser(1);
       IUser savedUser = userService.create(user);
        System.out.println();
    }

}
