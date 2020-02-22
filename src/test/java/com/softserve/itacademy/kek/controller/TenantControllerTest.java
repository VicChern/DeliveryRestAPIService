package com.softserve.itacademy.kek.controller;

import java.util.UUID;

import com.google.gson.Gson;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;

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
    private AddressDto addressDto;


    @InjectMocks
    private TenantController controller;

    private MockMvc mockMvc;

    @BeforeTest
    public void setup() {
        TenantDetailsDto detailsDto = new TenantDetailsDto("some payload", "http://awesomepicture.com");
        tenantDto = new TenantDto(UUID.fromString("guid12345qwawt"), UUID.fromString("Petro"), "pict", detailsDto);
        tenantPropertiesDto = new TenantPropertiesDto(
                "guid12345qwawt", "glovo", "additional info", "workingDay", "Wednesday");
        addressDto = new AddressDto(UUID.fromString("guid12345qwert"), "alias", "Leipzigzskaya 15v", "Some notes...");

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
                .andExpect(content().contentType("application/vnd.softserve.tenantproperty+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwawt"))
                .andExpect(jsonPath("$.tenant").value("glovo"))
                .andExpect(jsonPath("$.type").value("additional info"))
                .andExpect(jsonPath("$.key").value("workingDay"))
                .andExpect(jsonPath("$.value").value("Wednesday"));

    }

    @Test
    public void modifyTenantPropertyTest() throws Exception {
        mockMvc.perform(put("/tenants/2/properties/2")
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
    public void deleteTenantPropertyTest() throws Exception {
        mockMvc.perform(delete("/tenants/3/properties/3"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTenantAddressesTest() throws Exception {
        mockMvc.perform(get("/tenants/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$[0].guid").value("guid12345qwert"))
                .andExpect(jsonPath("$[0].alias").value("alias"))
                .andExpect(jsonPath("$[0].address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$[0].notes").value("Some notes..."));
    }

    @Test
    public void addTenantAddressesTest() throws Exception {
        mockMvc.perform(post("/tenants/1/addresses")
                .contentType("application/vnd.softserve.address+json")
                .accept("application/vnd.softserve.address+json")
                .content(gson.toJson(addressDto)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$.notes").value("Some notes..."));
    }

    @Test
    public void getTenantAddressTest() throws Exception {
        mockMvc.perform(get("/tenants/1/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$.notes").value("Some notes..."));
    }

    @Test
    public void modifyTenantAddressTest() throws Exception {
        mockMvc.perform(put("/tenants/2/addresses/2")
                .contentType("application/vnd.softserve.address+json")
                .accept("application/vnd.softserve.address+json")
                .content(gson.toJson(addressDto)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$.notes").value("Some notes..."));
    }

    @Test
    public void deleteTenantAddressTest() throws Exception {
        mockMvc.perform(delete("/tenants/3/addresses/3"))
                .andExpect(status().isOk());
    }

}

