package com.softserve.itacademy.kek.controller;

import com.google.gson.Gson;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class UserControllerTest {
    private final Gson gson = new Gson();
    private UserDto userDto;
    private AddressDto addressDto;

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;

    @BeforeTest
    public void setup() {
        DetailsDto detailsDto = new DetailsDto("some payload", "http://awesomepicture.com");
        userDto = new UserDto("guid12345qwert", "Petro", "pict", "pict@email.com", "(098)123-45-67", detailsDto);
        addressDto = new AddressDto("guid12345qwert", "alias", "Leipzigzskaya 15v", "Some notes...");

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getUserListTest() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.user+json"))
                .andExpect(jsonPath("$[0].guid").value("guid12345qwert"))
                .andExpect(jsonPath("$[0].name").value("Petro"))
                .andExpect(jsonPath("$[0].nickname").value("pict"))
                .andExpect(jsonPath("$[0].email").value("pict@email.com"))
                .andExpect(jsonPath("$[0].phone").value("(098)123-45-67"))
                .andExpect(jsonPath("$[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$[0].details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void addUserTest() throws Exception {
        mockMvc.perform(post("/users")
                .contentType("application/vnd.softserve.user+json")
                .accept("application/vnd.softserve.user+json")
                .content(gson.toJson(userDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.name").value("Petro"))
                .andExpect(jsonPath("$.nickname").value("pict"))
                .andExpect(jsonPath("$.email").value("pict@email.com"))
                .andExpect(jsonPath("$.phone").value("(098)123-45-67"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void getUserTest() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.user+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.name").value("Petro"))
                .andExpect(jsonPath("$.nickname").value("pict"))
                .andExpect(jsonPath("$.email").value("pict@email.com"))
                .andExpect(jsonPath("$.phone").value("(098)123-45-67"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void modifyUserTest() throws Exception {
        mockMvc.perform(put("/users/2")
                .contentType("application/vnd.softserve.user+json")
                .accept("application/vnd.softserve.user+json")
                .content(gson.toJson(userDto)))
                .andExpect(status().isAccepted())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.name").value("Petro"))
                .andExpect(jsonPath("$.nickname").value("pict"))
                .andExpect(jsonPath("$.email").value("pict@email.com"))
                .andExpect(jsonPath("$.phone").value("(098)123-45-67"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users/3"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserAddressesTest() throws Exception {
        mockMvc.perform(get("/users/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$[0].guid").value("guid12345qwert"))
                .andExpect(jsonPath("$[0].alias").value("alias"))
                .andExpect(jsonPath("$[0].address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$[0].notes").value("Some notes..."));
    }

    @Test
    public void addUserAddressesTest() throws Exception {
        mockMvc.perform(post("/users/1/addresses")
                .contentType("application/vnd.softserve.address+json")
                .accept("application/vnd.softserve.address+json")
                .content(gson.toJson(addressDto)))
                .andExpect(status().isAccepted())
                .andExpect(status().isAccepted())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$.notes").value("Some notes..."));
    }

    @Test
    public void getUserAddressTest() throws Exception {
        mockMvc.perform(get("/users/1/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$.notes").value("Some notes..."));
    }

    @Test
    public void modifyUserAddressTest() throws Exception {
        mockMvc.perform(put("/users/2/addresses/1")
                .contentType("application/vnd.softserve.address+json")
                .accept("application/vnd.softserve.address+json")
                .content(gson.toJson(addressDto)))
                .andExpect(status().isAccepted())
                .andExpect(status().isAccepted())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$.guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$.notes").value("Some notes..."));
    }

    @Test
    public void deleteUserAddressTest() throws Exception {
        mockMvc.perform(delete("/users/3/addresses/1"))
                .andExpect(status().isOk());
    }
}
