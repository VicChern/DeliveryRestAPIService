package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.GlobalProperties;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.repositories.GlobalPropertiesRepository;
import com.softserve.itacademy.kek.repositories.PropertyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getGlobalProperty;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getPropertyType;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class GlobalPropertiesTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private GlobalPropertiesRepository propertiesRepository;
    @Autowired
    private PropertyTypeRepository typeRepository;

    private GlobalProperties properties1;
    private GlobalProperties properties2;

    @DataProvider(name = "illegal_keys")
    public static Object[][] keys() {
        return new Object[][]{{createRandomLetterString(MAX_LENGTH_256 + 1)}, {""}};
    }

    @DataProvider(name = "illegal_values")
    public static Object[][] values() {
        return new Object[][]{{createRandomLetterString(MAX_LENGTH_4096 + 1)}, {""}};
    }

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {
        PropertyType propertyType1 = getPropertyType();
        properties1 = getGlobalProperty(propertyType1);

        PropertyType propertyType2 = getPropertyType();
        properties2 = getGlobalProperty(propertyType2);
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        propertiesRepository.deleteAll();
    }

    @Test(groups = {"integration-tests"})
    public void testGlobalPropertiesIsSavedWithValidFields() {
        propertiesRepository.save(properties1);
        Long id = properties1.getIdProperty();
        //when
        Optional<GlobalProperties> savedProperty = propertiesRepository.findById(id);
        //then
        Assert.assertNotNull(savedProperty.orElse(null));
        Assert.assertEquals(savedProperty.get().getIdProperty(), id);
        Assert.assertNotNull(savedProperty.get().getKey());
    }

    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testGlobalPropertiesIsNotSavedWithNullKey() {
        String key = null;
        properties1.setKey(key);
        //when
        propertiesRepository.save(properties1);
    }

    @Test(groups = {"integration-tests"}, dataProvider = "illegal_keys", expectedExceptions = ConstraintViolationException.class)
    public void testGlobalPropertiesIsNotSavedWithKeyMoreThanMaxLengthOrEmpty(String key) {
        properties1.setKey(key);
        //when
        propertiesRepository.save(properties1);
    }

    @Test(groups = {"integration-tests"}, dataProvider = "illegal_values", expectedExceptions = {ConstraintViolationException.class})
    public void testGlobalPropertiesIsNotSavedWithValueMoreThanMaxLengthOrEmpty(String value) {
        properties1.setValue(value);
        //when
        propertiesRepository.save(properties1);
    }

    @Test(groups = {"integration-tests"}, expectedExceptions = {ConstraintViolationException.class})
    public void testGlobalPropertiesIsNotSavedWithNullValue() {
        String value = null;
        properties1.setValue(value);
        //when
        propertiesRepository.save(properties1);
    }

    @Test(groups = {"integration-tests"}, expectedExceptions = DataIntegrityViolationException.class)
    public void testGlobalPropertiesIsSavedWithUniqueKey() {
        //given
        propertiesRepository.save(properties1);
        String property1Key = properties1.getKey();
        properties2.setKey(property1Key);
        //when
        propertiesRepository.save(properties2);
    }

}
