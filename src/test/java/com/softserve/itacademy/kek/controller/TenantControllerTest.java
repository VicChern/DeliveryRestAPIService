package com.softserve.itacademy.kek.controller;

import com.google.gson.Gson;
import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class TenantControllerTest {
    private final Gson gson = new Gson();
    private TenantDto tenantDto;
    private TenantPropertiesDto tenantPropertiesDto;

    @InjectMocks
    private TenantController controller;

    private MockMvc mockMvc;

    @BeforeTest
    public void setup() {
        TenantDetailsDto detailsDto = new TenantDetailsDto("some payload", "http://awesomepicture.com");
        tenantDto = new TenantDto("guid12345qwawt", "Petro", "pict", detailsDto);
        tenantPropertiesDto = new TenantPropertiesDto(
                "guid12345qwawt", "glovo", "additional info", "workingDay", "Wednesday");

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getTenantListTest() throws Exception {
        mockMvc.perform(get("/tenants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.tenant+json"))
                .andExpect(jsonPath("$[0].guid").value("guid12345qwawt"))
                .andExpect(jsonPath("$[0].owner").value("Petro"))
                .andExpect(jsonPath("$[0].name").value("pict"))
                .andExpect(jsonPath("$[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$[0].details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void addTenantTest() throws Exception {
        mockMvc.perform(post("/tenants")
                .contentType("application/vnd.softserve.tenant+json")
                .accept("application/vnd.softserve.tenant+json")
                .content(gson.toJson(tenantDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.guid").value("guid12345qwawt"))
                .andExpect(jsonPath("$.owner").value("Petro"))
                .andExpect(jsonPath("$.name").value("pict"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void getTenantTest() throws Exception {
        mockMvc.perform(get("/tenants/guid12345qwawt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.tenant+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwawt"))
                .andExpect(jsonPath("$.owner").value("Petro"))
                .andExpect(jsonPath("$.name").value("pict"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("http://awesomepicture.com"));
    }


    @Test
    public void modifyTenantTest() throws Exception {
        mockMvc.perform(put("/tenants/guid12345qwawt")
                .contentType("application/vnd.softserve.tenant+json")
                .accept("application/vnd.softserve.tenant+json")
                .content(gson.toJson(tenantDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.guid").value("guid12345qwawt"))
                .andExpect(jsonPath("$.owner").value("Petro"))
                .andExpect(jsonPath("$.name").value("pict"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("http://awesomepicture.com"));
    }


    @Test
    public void deleteTenantTest() throws Exception {
        mockMvc.perform(delete("/tenants/guid12345qwawt"))
                .andExpect(status().isOk());
    }


    @Test
    public void getTenantPropertiesTest() throws Exception {
        mockMvc.perform(get("/tenants/guid12345qwawt/properties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.tenantproperty+json"))
                .andExpect(jsonPath("$[0].guid").value("guid12345qwawt"))
                .andExpect(jsonPath("$[0].tenant").value("glovo"))
                .andExpect(jsonPath("$[0].type").value("additional info"))
                .andExpect(jsonPath("$[0].key").value("workingDay"))
                .andExpect(jsonPath("$[0].value").value("Wednesday"));
    }


    @Test
    public void addTenantPropertiesTest() throws Exception {
        mockMvc.perform(post("/tenants/guid12345qwawt/properties")
                .contentType("application/vnd.softserve.tenantproperty+json")
                .accept("application/vnd.softserve.tenantproperty+json")
                .content(gson.toJson(tenantPropertiesDto)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType("application/vnd.softserve.tenantproperty+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwawt"))
                .andExpect(jsonPath("$.tenant").value("glovo"))
                .andExpect(jsonPath("$.type").value("additional info"))
                .andExpect(jsonPath("$.key").value("workingDay"))
                .andExpect(jsonPath("$.value").value("Wednesday"));

    }

    @Test
    public void getTenantPropertyTest() throws Exception {
        mockMvc.perform(get("/tenants/1/properties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void modifyTenantPropertyTest() throws Exception {
        mockMvc.perform(put("/tenants/2/properties/2")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("value"));
    }

    @Test
    public void deleteTenantPropertyTest() throws Exception {
        mockMvc.perform(delete("/tenants/3/properties/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("3"))
                .andExpect(jsonPath("$.status").value("deleted"));
    }

    @Test
    public void getTenantAddressesTest() throws Exception {
        mockMvc.perform(get("/tenants/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void addTenantAddressesTest() throws Exception {
        mockMvc.perform(post("/tenants/1/addresses")
                .content("{'item': '15v, Leipzigzskaya st, Kiev'}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("15v, Leipzigzskaya st, Kiev"));
    }

    @Test
    public void getTenantAddressTest() throws Exception {
        mockMvc.perform(get("/tenants/1/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void modifyTenantAddressTest() throws Exception {
        mockMvc.perform(put("/tenants/2/addresses/2")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("value"));
    }

    @Test
    public void deleteTenantAddressTest() throws Exception {
        mockMvc.perform(delete("/tenants/3/addresses/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("3"))
                .andExpect(jsonPath("$.status").value("deleted"));
    }

}

