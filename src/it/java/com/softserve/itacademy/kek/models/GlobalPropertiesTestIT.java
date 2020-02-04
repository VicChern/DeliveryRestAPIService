package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.GlobalPropertiesRepository;
import com.softserve.itacademy.kek.repositories.PropertyTypeRepository;
import com.softserve.itacademy.kek.utils.ITTestUtils;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class GlobalPropertiesTestIT extends AbstractTestNGSpringContextTests {
    private static final int MAX_KEY_LENGTH = 256;
    private static final int MAX_VALUE_LENGTH = 4096;

    @Autowired
    GlobalPropertiesRepository propertiesRepository;
    @Autowired
    PropertyTypeRepository typeRepository;

    GlobalProperties properties1;
    GlobalProperties properties2;

    @DataProvider(name="keys")
    public static Object[][] keys(){
        return new Object[][]{{null},{RandomString.make(MAX_KEY_LENGTH + 1)}};
    }

    @DataProvider(name="values")
    public static Object[][] values(){
        return new Object[][]{{null},{RandomString.make(MAX_VALUE_LENGTH + 1)}};
    }

    @BeforeMethod
    public void setUp() {
        properties1 = ITTestUtils.getGlobalProperty(createTransientFields());
        properties2 = ITTestUtils.getGlobalProperty(createTransientFields());
    }

    @Test
    public void saveProperty_FindById_thenCorrect() {
        propertiesRepository.save(properties1);
        Long id = properties1.getIdProperty();
        //when
        Optional<GlobalProperties> savedProperty = propertiesRepository.findById(id);
        //then
        Assert.assertNotNull(savedProperty.orElse(null));
        Assert.assertEquals(savedProperty.get().getIdProperty(), id);
    }

    @Test(dataProvider = "keys", expectedExceptions = DataIntegrityViolationException.class)
    public void savePropertyType_KeyIsNull_ExceptionThrown(String key) {
        properties1.setKey(key);
        //when
        propertiesRepository.save(properties1);
    }

    @Test(dataProvider = "values", expectedExceptions = DataIntegrityViolationException.class)
    public void savePropertyType_ValueIsNull_ExceptionThrown(String value) {
        properties1.setValue(value);
        //when
        propertiesRepository.save(properties1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveProperty_KeyIsDuplicated_ExceptionThrown() {
        //given
        propertiesRepository.save(properties1);
        String property1Key = properties1.getKey();
        properties2.setKey(property1Key);
        //when
        propertiesRepository.save(properties2);
    }

    private PropertyType createTransientFields() {
        PropertyType type = ITTestUtils.getPropertyType();
        typeRepository.save(type);
        return type;
    }
}
