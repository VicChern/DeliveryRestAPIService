package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.PropertyTypeRepository;
import com.softserve.itacademy.kek.utils.ITTestUtils;
import net.bytebuddy.utility.RandomString;
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

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class PropertyTypeTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private PropertyTypeRepository repository;

    private PropertyType propertyType1;
    private PropertyType propertyType2;

    @DataProvider(name="illegal_names")
    public static Object[][] names(){
        return new Object[][]{{RandomString.make(MAX_LENGTH_256 + 1)}, {""}};
    }

    @BeforeMethod
    public void setUp() {
        propertyType1 = ITTestUtils.getPropertyType();
        propertyType2 = ITTestUtils.getPropertyType();
    }

    @AfterMethod
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void testPropertyTypeWasSavedAndFindById() {
        repository.save(propertyType1);
        Long id = propertyType1.getIdPropertyType();
        //when
        Optional<PropertyType> savedPropertyType = repository.findById(id);
        //then
        Assert.assertNotNull(savedPropertyType.orElse(null));
        Assert.assertEquals(savedPropertyType.get().getIdPropertyType(), id);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void validatePropertyTypeIsNotSavedWithNullName() {
        propertyType1.setName(null);
        //when
        repository.save(propertyType1);
    }

    @Test(dataProvider = "illegal_names",expectedExceptions = ConstraintViolationException.class)
    public void validatePropertyTypeIsNotSavedWithEmptyOrLongName(String name) {
        propertyType1.setName(name);
        //when
        repository.save(propertyType1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void saveProperty_NameIsDuplicated_ExceptionThrown() {
        //given
        repository.save(propertyType1);
        String propertyType1Name = propertyType1.getName();
        propertyType2.setName(propertyType1Name);
        //when
        repository.save(propertyType2);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void savePropertyType_SchemaIsNull_ExceptionThrown() {
        propertyType1.setSchema(null);
        //when
        repository.save(propertyType1);
    }
}
