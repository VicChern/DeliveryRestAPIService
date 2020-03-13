package com.softserve.itacademy.kek.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.models.IAddress;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.ITenantProperties;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Address;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantDetails;
import com.softserve.itacademy.kek.models.impl.TenantProperties;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;
import com.softserve.itacademy.kek.services.IAddressService;
import com.softserve.itacademy.kek.services.ITenantPropertiesService;
import com.softserve.itacademy.kek.services.ITenantService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class TenantControllerTest {
    private final String tenantJson = "{\n" +
            "  \"guid\": \"48c5db5c-af58-4350-874e-b99b33c6af86\",\n" +
            "  \"owner\": \"10241624-9ea7-4777-99b5-54ab6d591c44\",\n" +
            "  \"name\": \"kek\",\n" +
            "  \"details\": {\n" +
            "    \"payload\": \"some payload\",\n" +
            "    \"imageUrl\": \"http://awesomepicture.com\"\n" +
            "  }\n" +
            "}";
    private final String tenantPropertyJson = "{\n" +
            "  \"guid\": \"48c5db5c-af58-4350-874e-b99b33c6af86\",\n" +
            "  \"key\": \"string\",\n" +
            "  \"propertyType\": {\n" +
            "    \"name\": \"string\",\n" +
            "    \"schema\": \"string\"\n" +
            "  },\n" +
            "  \"value\": \"string\"\n" +
            "}";
    private final String addressListJson = "{\n" +
            "  \"list\": [\n" +
            "    {\n" +
            "      \"alias\": \"random building\",\n" +
            "      \"address\": \"USA, White House\",\n" +
            "      \"notes\": \"smth close to Trump\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    private final String addressJson = "{\n" +
            "\t\"guid\": \"48c5db5c-af58-4350-874e-b99b33c6af86\",\n" +
            "    \"alias\": \"alias\",\n" +
            "    \"address\": \"address\",\n" +
            "    \"notes\": \"notes\"\n" +
            "}";

    @InjectMocks
    private TenantController controller;
    @Spy
    private ITenantService tenantService;
    @Spy
    private IAddressService addressService;
    @Spy
    ITenantPropertiesService tenantPropertiesService;
    private MockMvc mockMvc;

    private User user;
    private List<IUser> userList;
    private Tenant tenant;
    private List<ITenant> tenantList;
    private Address address;
    private List<IAddress> addressList;
    private TenantProperties tenantProperties;
    private List<ITenantProperties> tenantPropertiesList;

    @BeforeTest
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        UserDetails userDetails = new UserDetails();
        userDetails.setImageUrl("pic url");
        userDetails.setPayload("some payload");

        user = new User();
        user.setGuid(UUID.fromString("10241624-9ea7-4777-99b5-54ab6d591c44"));
        user.setName("name");
        user.setNickname("nickname");
        user.setEmail("name@email.com");
        user.setPhoneNumber("380981234567");
        user.setUserDetails(userDetails);

        userList = new ArrayList<>();
        userList.add(user);

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setImageUrl("http://awesomepicture.com");
        tenantDetails.setPayload("some payload");

        tenant = new Tenant();
        tenant.setGuid(UUID.fromString("48c5db5c-af58-4350-874e-b99b33c6af86"));
        tenant.setTenantOwner(user);
        tenant.setName("Kek");
        tenant.setTenantDetails(tenantDetails);

        tenantList = new ArrayList<>();
        tenantList.add(tenant);

        PropertyType propertyType = new PropertyType();
        propertyType.setName("string");
        propertyType.setSchema("string");

        tenantProperties = new TenantProperties();
        tenantProperties.setGuid(UUID.fromString("48c5db5c-af58-4350-874e-b99b33c6af86"));
        tenantProperties.setKey("string");
        tenantProperties.setPropertyType(propertyType);
        tenantProperties.setValue("string");

        tenantPropertiesList = new ArrayList<>();
        tenantPropertiesList.add(tenantProperties);

        address = new Address();
        address.setGuid(UUID.fromString("48c5db5c-af58-4350-874e-b99b33c6af86"));
        address.setAlias("alias");
        address.setAddress("address");
        address.setNotes("notes");

        addressList = new ArrayList<>();
        addressList.add(address);
    }

    @Test
    public void getTenantListTest() throws Exception {
        when(tenantService.getAll()).thenReturn(tenantList);

        mockMvc.perform(get("/tenants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.TENANT_LIST))
                .andExpect(jsonPath("$.list[0].guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.list[0].owner").value("10241624-9ea7-4777-99b5-54ab6d591c44"))
                .andExpect(jsonPath("$.list[0].name").value("Kek"))
                .andExpect(jsonPath("$.list[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$.list[0].details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void addTenantTest() throws Exception {
        when(tenantService.create(any(TenantDto.class))).thenReturn(tenant);

        mockMvc.perform(post("/tenants")
                .contentType(KekMediaType.TENANT)
                .accept(KekMediaType.TENANT)
                .content(tenantJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.owner").value("10241624-9ea7-4777-99b5-54ab6d591c44"))
                .andExpect(jsonPath("$.name").value("Kek"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void getTenantTest() throws Exception {
        when(tenantService.getByGuid(any(UUID.class))).thenReturn(tenant);

        mockMvc.perform(get("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.TENANT))
                .andExpect(jsonPath("$.guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.owner").value("10241624-9ea7-4777-99b5-54ab6d591c44"))
                .andExpect(jsonPath("$.name").value("Kek"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void modifyTenantTest() throws Exception {
        when(tenantService.update(any(TenantDto.class), any(UUID.class))).thenReturn(tenant);

        mockMvc.perform(put("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86")
                .contentType(KekMediaType.TENANT)
                .accept(KekMediaType.TENANT)
                .content(tenantJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.owner").value("10241624-9ea7-4777-99b5-54ab6d591c44"))
                .andExpect(jsonPath("$.name").value("Kek"))
                .andExpect(jsonPath("$.details.payload").value("some payload"))
                .andExpect(jsonPath("$.details.imageUrl").value("http://awesomepicture.com"));
    }


    @Test
    public void deleteTenantTest() throws Exception {
        mockMvc.perform(delete("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getTenantPropertiesTest() throws Exception {
        when(tenantPropertiesService.getAllForTenant(any(UUID.class))).thenReturn(tenantPropertiesList);

        mockMvc.perform(get("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/properties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.TENANT_PROPERTY))
                .andExpect(jsonPath("$.list.[0].guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.list.[0].key").value("string"))
                .andExpect(jsonPath("$.list.[0].propertyType.name").value("string"))
                .andExpect(jsonPath("$.list.[0].propertyType.schema").value("string"))
                .andExpect(jsonPath("$.list.[0].value").value("string"));
    }

    @Test
    public void addTenantPropertiesTest() throws Exception {
        when(tenantPropertiesService.create(anyList(), any(UUID.class))).thenReturn(tenantPropertiesList);

        mockMvc.perform(post("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/properties")
                .contentType(KekMediaType.TENANT_PROPERTY)
                .accept(KekMediaType.TENANT_PROPERTY)
                .content(tenantPropertyJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(KekMediaType.TENANT_PROPERTY))
                .andExpect(jsonPath("$.list.[0].guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.list.[0].key").value("string"))
                .andExpect(jsonPath("$.list.[0].propertyType.name").value("string"))
                .andExpect(jsonPath("$.list.[0].propertyType.schema").value("string"))
                .andExpect(jsonPath("$.list.[0].value").value("string"));

    }

    @Test
    public void getTenantPropertyTest() throws Exception {
        when(tenantPropertiesService.get(any(UUID.class), any(UUID.class))).thenReturn(tenantProperties);

        mockMvc.perform(get("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/properties/48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.TENANT_PROPERTY))
                .andExpect(jsonPath("$.guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.key").value("string"))
                .andExpect(jsonPath("$.propertyType.name").value("string"))
                .andExpect(jsonPath("$.propertyType.schema").value("string"))
                .andExpect(jsonPath("$.value").value("string"));

    }

    @Test
    public void modifyTenantPropertyTest() throws Exception {
        when(tenantPropertiesService.update(any(UUID.class), any(UUID.class), any(ITenantProperties.class))).thenReturn(tenantProperties);

        mockMvc.perform(put("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/properties/48c5db5c-af58-4350-874e-b99b33c6af86")
                .contentType(KekMediaType.TENANT_PROPERTY)
                .accept(KekMediaType.TENANT_PROPERTY)
                .content(tenantPropertyJson))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(KekMediaType.TENANT_PROPERTY))
                .andExpect(jsonPath("$.guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.key").value("string"))
                .andExpect(jsonPath("$.propertyType.name").value("string"))
                .andExpect(jsonPath("$.propertyType.schema").value("string"))
                .andExpect(jsonPath("$.value").value("string"));

    }

    @Test
    public void deleteTenantPropertyTest() throws Exception {
        mockMvc.perform(delete("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/properties/48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTenantAddressesTest() throws Exception {
        when(addressService.getAllForTenant(any(UUID.class))).thenReturn(addressList);

        mockMvc.perform(get("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.ADDRESS_LIST))
                .andExpect(jsonPath("$.list[0].guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.list[0].alias").value("alias"))
                .andExpect(jsonPath("$.list[0].address").value("address"))
                .andExpect(jsonPath("$.list[0].notes").value("notes"));
    }

    @Test
    public void addTenantAddressesTest() throws Exception {
        when(addressService.createForTenant(any(IAddress.class), any(UUID.class))).thenReturn(address);

        mockMvc.perform(post("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/addresses")
                .contentType(KekMediaType.ADDRESS)
                .accept(KekMediaType.ADDRESS)
                .content(addressListJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(KekMediaType.ADDRESS))
                .andExpect(jsonPath("$.list[0].guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.list[0].alias").value("alias"))
                .andExpect(jsonPath("$.list[0].address").value("address"))
                .andExpect(jsonPath("$.list[0].notes").value("notes"));
    }

    @Test
    public void getTenantAddressTest() throws Exception {
        when(addressService.getForTenant(any(UUID.class), any(UUID.class))).thenReturn(address);

        mockMvc.perform(get("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/addresses/48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.ADDRESS))
                .andExpect(jsonPath("$.guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.notes").value("notes"));
    }

    @Test
    public void modifyTenantAddressTest() throws Exception {
        when(addressService.updateForTenant(any(IAddress.class), any(UUID.class), any(UUID.class))).thenReturn(address);

        mockMvc.perform(put("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/addresses/48c5db5c-af58-4350-874e-b99b33c6af86")
                .contentType(KekMediaType.ADDRESS)
                .accept(KekMediaType.ADDRESS)
                .content(addressJson))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(KekMediaType.ADDRESS))
                .andExpect(jsonPath("$.guid").value("48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.notes").value("notes"));
    }

    @Test
    public void deleteTenantAddressTest() throws Exception {
        mockMvc.perform(delete("/tenants/48c5db5c-af58-4350-874e-b99b33c6af86/addresses/48c5db5c-af58-4350-874e-b99b33c6af86"))
                .andExpect(status().isOk());
    }

}

