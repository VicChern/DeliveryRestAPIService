package com.softserve.itacademy.kek.controller;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.GlobalPropertiesDto;
import com.softserve.itacademy.kek.models.IGlobalProperties;
import com.softserve.itacademy.kek.models.impl.GlobalProperties;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.services.IGlobalPropertiesService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class GlobalPropertiesControllerTest {
    private final String globalPropertyJson = "{\n" +
            "  \"key\": \"someKey\",\n" +
            "  \"propertyType\": {\n" +
            "    \"name\": \"pushka\",\n" +
            "    \"schema\": \"string\"\n" +
            "  },\n" +
            "  \"value\": \"gonka\"\n" +
            "}";

    @InjectMocks
    private GlobalPropertiesController controller;
    @Spy
    private IGlobalPropertiesService globalPropertiesService;
    private MockMvc mockMvc;

    private GlobalProperties globalProperties;
    List<IGlobalProperties> globalPropertiesList;

    @BeforeTest
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        PropertyType propertyType = new PropertyType();
        propertyType.setName("string");
        propertyType.setSchema("string");

        globalProperties = new GlobalProperties();
        globalProperties.setKey("string");
        globalProperties.setValue("string");
        globalProperties.setPropertyType(propertyType);

        globalPropertiesList = new ArrayList<>();
        globalPropertiesList.add(globalProperties);
    }

    @Test
    public void getGlobalPropertiesTest() throws Exception {
        when(globalPropertiesService.getAll()).thenReturn(globalPropertiesList);

        mockMvc.perform(get("/globalProperties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.GLOBAL_PROPERTY))
                .andExpect(jsonPath("$.globalPropertiesDtoList.[0].key").value("string"))
                .andExpect(jsonPath("$.globalPropertiesDtoList.[0].propertyType.name").value("string"))
                .andExpect(jsonPath("$.globalPropertiesDtoList.[0].propertyType.schema").value("string"))
                .andExpect(jsonPath("$.globalPropertiesDtoList.[0].value").value("string"));
    }

    @Test
    public void addGlobalPropertyTest() throws Exception {
        when(globalPropertiesService.create(any(GlobalPropertiesDto.class))).thenReturn(globalProperties);

        mockMvc.perform(post("/globalProperties")
                .contentType(KekMediaType.GLOBAL_PROPERTY)
                .accept(KekMediaType.GLOBAL_PROPERTY)
                .content(globalPropertyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.key").value("string"))
                .andExpect(jsonPath("$.propertyType.name").value("string"))
                .andExpect(jsonPath("$.propertyType.schema").value("string"))
                .andExpect(jsonPath("$.value").value("string"));
    }

    @Test
    public void modifyGlobalPropertyTest() throws Exception {
        when(globalPropertiesService.update(any(GlobalPropertiesDto.class), anyString())).thenReturn(globalProperties);

        mockMvc.perform(put("/globalProperties/key")
                .contentType(KekMediaType.GLOBAL_PROPERTY)
                .accept(KekMediaType.GLOBAL_PROPERTY)
                .content(globalPropertyJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.key").value("string"))
                .andExpect(jsonPath("$.propertyType.name").value("string"))
                .andExpect(jsonPath("$.propertyType.schema").value("string"))
                .andExpect(jsonPath("$.value").value("string"));
    }

    @Test
    public void deleteGlobalProperty() throws Exception {
        mockMvc.perform(delete("/globalProperties/key"))
                .andExpect(status().isNoContent());
    }

}
