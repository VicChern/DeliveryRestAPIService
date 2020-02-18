package com.softserve.itacademy.kek.controller;

import java.util.Arrays;
import java.util.UUID;

import com.google.gson.Gson;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.AddressListDto;
import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.dto.UserListDto;

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
    private UserListDto userListDto;
    private AddressDto addressDto;
    private AddressListDto addressListDto;

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;

    @BeforeTest
    public void setup() {
        DetailsDto detailsDto = new DetailsDto("some payload", "http://awesomepicture.com");
        userDto = new UserDto(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"), "Petro", "pict", "pict@email.com", "(098)123-45-67", detailsDto);
        userListDto = new UserListDto().addUser(userDto);
        addressDto = new AddressDto("820671c6-7e2c-4de3-aeb8-42e6f84e6371", "alias", "Leipzigzskaya 15v", "Some notes...");
        addressListDto = new AddressListDto().addAddress(addressDto);


        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getUserListTest() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.userList+json"))
                .andExpect(jsonPath("$.userList[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.userList[0].name").value("Petro"))
                .andExpect(jsonPath("$.userList[0].nickname").value("pict"))
                .andExpect(jsonPath("$.userList[0].email").value("pict@email.com"))
                .andExpect(jsonPath("$.userList[0].phoneNumber").value("(098)123-45-67"))
                .andExpect(jsonPath("$.userList[0].userDetails.payload").value("some payload"))
                .andExpect(jsonPath("$.userList[0].userDetails.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void addUserTest() throws Exception {
        mockMvc.perform(post("/users")
                .contentType("application/vnd.softserve.userList+json")
                .accept("application/vnd.softserve.userList+json")
                .content(gson.toJson(userListDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userList[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.userList[0].name").value("Petro"))
                .andExpect(jsonPath("$.userList[0].nickname").value("pict"))
                .andExpect(jsonPath("$.userList[0].email").value("pict@email.com"))
                .andExpect(jsonPath("$.userList[0].phoneNumber").value("(098)123-45-67"))
                .andExpect(jsonPath("$.userList[0].userDetails.payload").value("some payload"))
                .andExpect(jsonPath("$.userList[0].userDetails.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void getUserTest() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.user+json"))
                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.name").value("Petro"))
                .andExpect(jsonPath("$.nickname").value("pict"))
                .andExpect(jsonPath("$.email").value("pict@email.com"))
                .andExpect(jsonPath("$.phoneNumber").value("(098)123-45-67"))
                .andExpect(jsonPath("$.userDetails.payload").value("some payload"))
                .andExpect(jsonPath("$.userDetails.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void modifyUserTest() throws Exception {
        mockMvc.perform(put("/users/2")
                .contentType("application/vnd.softserve.user+json")
                .accept("application/vnd.softserve.user+json")
                .content(gson.toJson(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
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
                .andExpect(content().contentType("application/vnd.softserve.addressList+json"))
                .andExpect(jsonPath("$.addressList[0].guid").value("guid12345qwert"))
                .andExpect(jsonPath("$.addressList[0].alias").value("alias"))
                .andExpect(jsonPath("$.addressList[0].address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$.addressList[0].notes").value("Some notes..."));
    }

    @Test
    public void addUserAddressesTest() throws Exception {
        System.out.println(gson.toJson(addressDto));
        mockMvc.perform(post("/users/1/addresses")
                .contentType("application/vnd.softserve.addressList+json")
                .accept("application/vnd.softserve.addressList+json")
                .content(gson.toJson(addressListDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.addressList+json"))
                .andExpect(jsonPath("$.addressList[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.addressList[0].alias").value("alias"))
                .andExpect(jsonPath("$.addressList[0].address").value("Leipzigzskaya 15v"))
                .andExpect(jsonPath("$.addressList[0].notes").value("Some notes..."));
    }

    @Test
    public void getUserAddressTest() throws Exception {
        mockMvc.perform(get("/users/1/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
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
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.address+json"))
                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
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
