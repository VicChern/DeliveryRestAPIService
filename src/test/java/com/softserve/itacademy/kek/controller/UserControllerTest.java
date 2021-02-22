package com.softserve.itacademy.kek.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.models.IAddress;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Address;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;
import com.softserve.itacademy.kek.services.IAddressService;
import com.softserve.itacademy.kek.services.IUserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class UserControllerTest {
    private final String userJson = "{\n" +
            "   \"name\":\"name\",\n" +
            "   \"nickname\":\"nickname\",\n" +
            "   \"email\":\"name@email.com\",\n" +
            "   \"phone\":\"380981234567\",\n" +
            "   \"details\":{\n" +
            "      \"payload\":\"Some payload\",\n" +
            "      \"imageUrl\":\"pic url\"\n" +
            "   }\n" +
            "}";
    private final String addressListJson = "{\n" +
            "  \"list\": [\n" +
            "    {\n" +
            "      \"alias\": \"random building\",\n" +
            "      \"address\": \"21, Hreschatyk\",\n" +
            "      \"notes\": \"smth close to Ukrposhta\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    private final String addressJson = "{\n" +
            "\t\"guid\": \"820671c6-7e2c-4de3-aeb8-42e6f84e6371\",\n" +
            "    \"alias\": \"alias\",\n" +
            "    \"address\": \"address\",\n" +
            "    \"notes\": \"notes\"\n" +
            "}";
    @InjectMocks
    private UserController controller;
    @Spy
    private IUserService userService;
    @Spy
    private IAddressService addressService;
    private MockMvc mockMvc;

    private User user;
    private List<IUser> userList;
    private Address address;
    private List<IAddress> addressList;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        UserDetails userDetails = new UserDetails();
        userDetails.setImageUrl("pic url");
        userDetails.setPayload("some payload");

        user = new User();
        user.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        user.setName("name");
        user.setNickname("nickname");
        user.setEmail("name@email.com");
        user.setPhoneNumber("380981234567");
        user.setUserDetails(userDetails);

        userList = new ArrayList<>();
        userList.add(user);

        address = new Address();
        address.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        address.setAlias("alias");
        address.setAddress("address");
        address.setNotes("notes");

        addressList = new ArrayList<>();
        addressList.add(address);
    }

    @Test
    public void getUserListTest() throws Exception {
        when(userService.getAll()).thenReturn(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.USER_LIST))
                .andExpect(jsonPath("$.list[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.list[0].name").value("name"))
                .andExpect(jsonPath("$.list[0].nickname").value("nickname"))
                .andExpect(jsonPath("$.list[0].email").value("name@email.com"))
                .andExpect(jsonPath("$.list[0].phone").value("380981234567"))
                .andExpect(jsonPath("$.list[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$.list[0].details.imageUrl").value("pic url"));
    }

    @Test
    public void addUserTest() throws Exception {
        when(userService.create(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(KekMediaType.USER)
                .accept(KekMediaType.USER)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.email").value("name@email.com"))
                .andExpect(jsonPath("$.phone").value("380981234567"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("pic url"));
    }

    @Test
    public void getUserTest() throws Exception {
        when(userService.getByGuid(any(UUID.class))).thenReturn(user);

        mockMvc.perform(get("/users/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.USER))
                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.email").value("name@email.com"))
                .andExpect(jsonPath("$.phone").value("380981234567"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("pic url"));
    }

    @Test
    public void modifyUserTest() throws Exception {
        when(userService.update(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(put("/users/820671c6-7e2c-4de3-aeb8-42e6f84e6371")
                .contentType(KekMediaType.USER)
                .accept(KekMediaType.USER)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.email").value("name@email.com"))
                .andExpect(jsonPath("$.phone").value("380981234567"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("pic url"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getUserAddressesTest() throws Exception {
        when(addressService.getAllForUser(any(UUID.class))).thenReturn(addressList);

        mockMvc.perform(get("/users/820671c6-7e2c-4de3-aeb8-42e6f84e6371/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.ADDRESS_LIST))
                .andExpect(jsonPath("$.list[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.list[0].alias").value("alias"))
                .andExpect(jsonPath("$.list[0].address").value("address"))
                .andExpect(jsonPath("$.list[0].notes").value("notes"));
    }

    @Test
    public void addUserAddressesTest() throws Exception {
        when(addressService.createForUser(any(UUID.class), any(IAddress.class))).thenReturn(address);

        mockMvc.perform(post("/users/820671c6-7e2c-4de3-aeb8-42e6f84e6371/addresses")
                .contentType(KekMediaType.ADDRESS_LIST)
                .accept(KekMediaType.ADDRESS_LIST)
                .content(addressListJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(KekMediaType.ADDRESS_LIST))
                .andExpect(jsonPath("$.list[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.list[0].alias").value("alias"))
                .andExpect(jsonPath("$.list[0].address").value("address"))
                .andExpect(jsonPath("$.list[0].notes").value("notes"));
    }

    @Test
    public void getUserAddressTest() throws Exception {
        when(addressService.getForUser(any(UUID.class), any(UUID.class))).thenReturn(address);

        mockMvc.perform(get("/users/820671c6-7e2c-4de3-aeb8-42e6f84e6371/addresses/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.ADDRESS))
                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.notes").value("notes"));
    }

    @Test
    public void modifyUserAddressTest() throws Exception {
        when(addressService.updateForUser(any(UUID.class), any(UUID.class), any(IAddress.class))).thenReturn(address);

        mockMvc.perform(put("/users/820671c6-7e2c-4de3-aeb8-42e6f84e6371/addresses/820671c6-7e2c-4de3-aeb8-42e6f84e6371")
                .contentType(KekMediaType.ADDRESS)
                .accept(KekMediaType.ADDRESS)
                .content(addressJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.ADDRESS))
                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.notes").value("notes"));
    }

    @Test
    public void deleteUserAddressTest() throws Exception {
        mockMvc.perform(delete("/users/820671c6-7e2c-4de3-aeb8-42e6f84e6371/addresses/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(status().isOk());
    }
}
