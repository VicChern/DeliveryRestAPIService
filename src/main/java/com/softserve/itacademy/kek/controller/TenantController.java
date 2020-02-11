package com.softserve.itacademy.kek.controller;

import com.google.gson.Gson;
import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/tenants")
public class TenantController extends DefaultController {
    final Logger logger = LoggerFactory.getLogger(TenantController.class);
    private final Gson gson = new Gson();

    // Build Response (stub, temporary method)
    private String getJSON(String id, String status) {
        JSONObject json = new JSONObject();
        json.put("tenantID", id);
        json.put("status", status);
        return json.toString();
    }

    /**
     * Temporary method for TenantDto stub
     *
     * @return {@link TenantDto} stub
     */
    private TenantDto getTenantDtoStub() {
        TenantDetailsDto detailsDto = new TenantDetailsDto("some payload", "http://awesomepicture.com");
        return new TenantDto("guid12345qwawt", "Petro", "pict", detailsDto);
    }

    /**
     * Temporary method for TenantPropertiesDto stub
     *
     * @return {@link TenantPropertiesDto} stub
     */
    private TenantPropertiesDto getTenantPropertiesDtoStub() {
        return new TenantPropertiesDto(
                "guid12345qwawt", "glovo", "additional info", "workingDay", "Wednesday");
    }

    /**
     * Temporary method for AddressDto stub
     *
     * @return {@link AddressDto} stub
     */
    private AddressDto getTenantAddressDtoStub() {
        return new AddressDto("guid12345qwert", "alias", "Leipzigzskaya 15v", "Some notes...");

    }


    /**
     * Get information about tenants
     *
     * @return list of {@link TenantDto} objects as a JSON
     */
    @GetMapping(produces = "application/vnd.softserve.tenant+json")
    @ResponseStatus(HttpStatus.OK)
    public List<TenantDto> getTenantList() {
        logger.info("Client requested the list of all tenants");

        List<TenantDto> tenantList = new ArrayList<>();
        tenantList.add(getTenantDtoStub());

        logger.info("Sending list of all tenants to the client:\n" + gson.toJson(tenantList));
        return tenantList;
    }

    /**
     * Creates a new tenant
     *
     * @param body {@link TenantDto} object as a JSON
     * @return {@link TenantDto} object as a JSON
     */
    @PostMapping(consumes = "application/vnd.softserve.tenant+json", produces = "application/vnd.softserve.tenant+json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TenantDto addTenant(@RequestBody String body) {
        logger.info("Accepted requested to create a new tenant:\n" + body);

        TenantDto tenantDto = gson.fromJson(body, TenantDto.class);

        logger.info("Sending the created tenant to the client:\n" + gson.toJson(tenantDto));
        return tenantDto;
    }

    /**
     * Finds the specific tenant
     *
     * @param guid tenant ID from the URL
     * @return {@link TenantDto} object as a JSON
     */
    @GetMapping(value = "/{guid}", produces = "application/vnd.softserve.tenant+json")
    @ResponseStatus(HttpStatus.OK)
    public TenantDto getTenant(@PathVariable String guid) {
        logger.info("Client requested the tenant " + guid);

        TenantDto tenantDto = getTenantDtoStub();

        logger.info("Sending the specific tenant(" + guid + ") to the client");
        return tenantDto;
    }


    /**
     * Modifies information of the specified tenant
     *
     * @param guid tenant ID from the URL
     * @param body tenant object as a JSON
     * @return {@link TenantDto} object as a JSON
     */
    @PutMapping(value = "/{guid}", consumes = "application/vnd.softserve.tenant+json",
            produces = "application/vnd.softserve.tenant+json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TenantDto modifyTenant(@PathVariable String guid, @RequestBody String body) {
        logger.info("Accepted modified tenant from the client:\n" + body);

        TenantDto tenantDto = gson.fromJson(body, TenantDto.class);

        logger.info("Sending the modified tenant to the client:\n" + gson.toJson(tenantDto));
        return tenantDto;
    }

    /**
     * Removes the specified tenant
     *
     * @param guid tenant ID from the URL
     */
    @DeleteMapping("/{guid}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTenant(@PathVariable String guid) {
        logger.info("Accepted request to delete the tenant " + guid);

        logger.info("Tenant(" + guid + ") was successfully deleted");
    }

    /**
     * Find properties of the specific tenant
     *
     * @param guid tenant ID from URL
     * @return List of (@link TenantPropertiesDTO) objects as a JSON
     */
    @GetMapping(value = "/{guid}/properties", produces = "application/vnd.softserve.tenantproperty+json")
    @ResponseStatus(HttpStatus.OK)
    public List<TenantPropertiesDto> getTenantProperties(@PathVariable String guid) {
        logger.info("Client requested all the properties of the tenant " + guid);

        List<TenantPropertiesDto> tenantPropertiesList = new ArrayList<>();
        tenantPropertiesList.add(getTenantPropertiesDtoStub());

        logger.info("Sending the list of tenant's(" + guid + ") properties to the client:\n"
                + gson.toJson(tenantPropertiesList));

        return tenantPropertiesList;
    }

    /**
     * Add new properties of the specific tenant
     *
     * @param guid tenant ID from URL
     * @param body property object as a JSON
     * @return list of the {@link TenantPropertiesDto} objects as a JSON
     */
    @PostMapping(value = "/{guid}/properties", consumes = "application/vnd.softserve.tenantproperty+json",
            produces = "application/vnd.softserve.tenantproperty+json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TenantPropertiesDto addTenantProperties(@PathVariable String guid, @RequestBody String body) {
        logger.info("Accepted requested to create a new properties for tenant:" + guid + ":\n" + body);

        TenantPropertiesDto tenantPropertiesDto = gson.fromJson(body, TenantPropertiesDto.class);

        logger.info("Sending the created tenant's(" + guid + ") properties to the client");

        return tenantPropertiesDto;
    }

    /**
     * Finds specific property of the specific tenant
     *
     * @param guid     tenant ID from URL
     * @param propGuid ID of the tenant specific property
     * @return Specific tenant property {@link TenantPropertiesDto}
     */
    @GetMapping(value = "/{guid}/properties/{propguid}", produces = "application/vnd.softserve.tenantproperty+json")
    @ResponseStatus(HttpStatus.OK)
    public TenantPropertiesDto getTenantProperty(@PathVariable("guid") String guid, @PathVariable("propguid") String propGuid) {
        logger.info("Sending the tenant's(" + guid + ") specific property(" + propGuid + ") to the client");

        TenantPropertiesDto tenantPropertiesDto = getTenantPropertiesDtoStub();

        logger.info("Sending specific property of the tenant " + guid + " to the client:\n" + gson.toJson(tenantPropertiesDto));
        return tenantPropertiesDto;
    }

    /**
     * Modifies the specific tenant property
     *
     * @param guid     tenant ID from URN
     * @param propGuid ID of the specific tenant property
     * @param body     The tenant property to modify
     * @return The modified tenant property object{@link TenantPropertiesDto}
     */
    @PutMapping(value = "/{guid}/properties/{propguid}", consumes = "application/vnd.softserve.tenantproperty+json",
            produces = "application/vnd.softserve.tenantproperty+json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TenantPropertiesDto modifyTenantProperty(@PathVariable("guid") String guid, @PathVariable("propguid") String propGuid, @RequestBody String body) {
        logger.info("Sending the modified tenant's(" + guid + ") property(" + propGuid + ") to the client");

        TenantPropertiesDto tenantPropertiesDto = gson.fromJson(body, TenantPropertiesDto.class);

        logger.info("Sending the modified property of the tenant " + guid + " to the client:\n" + gson.toJson(tenantPropertiesDto));
        return tenantPropertiesDto;
    }

    /**
     * Deletes the specific tenant property
     *
     * @param guid     tenant ID from the URN
     * @param propGuid address ID from the URN
     */
    @DeleteMapping("/{guid}/properties/{propguid}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTenantProperty(@PathVariable("guid") String guid, @PathVariable("propguid") String propGuid) {
        logger.info("Accepted request to delete the property " + propGuid + " ot the tenant " + guid);

        logger.info("the property " + propGuid + " ot the tenant " + guid + " successfully deleted");
    }

    /**
     * Find addressees of the specific tenant
     *
     * @param guid tenant ID from URN tenant property
     * @return Specific List of tenant addresses{@link AddressDto}
     */
    @GetMapping(value = "/{guid}/addresses", produces = "application/vnd.softserve.address+json")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> getTenantAddresses(@PathVariable String guid) {
        logger.info("Client requested all the addresses" + guid);

        List<AddressDto> addressesList = new ArrayList<>();
        addressesList.add(getTenantAddressDtoStub());

        logger.info("Sending the list of addresses of the tenant " + guid + " to the client:\n" + gson.toJson(addressesList));
        return addressesList;
    }

    /**
     * Adds a new address for the specific tenant
     *
     * @param guid tenant ID from the URN
     * @param body address object as a JSON
     * @return {@link AddressDto} object as a JSON
     */
    @PostMapping(value = "/{guid}/addresses", consumes = "application/vnd.softserve.address+json",
            produces = "application/vnd.softserve.address+json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AddressDto addTenantAddresses(@PathVariable String guid, @RequestBody String body) {
        logger.info("Accepted requested to create a new address for tenant:" + guid + ":\n" + body);

        AddressDto addressDto = gson.fromJson(body, AddressDto.class);

        logger.info("Sending the created address of the tenant " + guid + " to the client:\n" + gson.toJson(addressDto));
        return addressDto;
    }

    /**
     * Finds specific address of the specific tenant
     *
     * @param guid     tenant ID from URN
     * @param addrGuid ID of the specific address
     * @return Specific tenant tenant property{@link TenantPropertiesDto}
     */
    @GetMapping(value = "/{guid}/addresses/{addrguid}", produces = "application/vnd.softserve.address+json")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getTenantAddress(@PathVariable("guid") String guid, @PathVariable("addrguid") String addrGuid) {
        logger.info("Client requested the address " + addrGuid + " of the tenant " + guid);

        AddressDto addressDto = getTenantAddressDtoStub();

        logger.info("Sending the address of the tenant " + guid + " to the client:\n" + gson.toJson(addressDto));
        return addressDto;
    }

    /**
     * Modifies the specific tenant address
     *
     * @param guid     tenant ID from URN
     * @param addrGuid ID of the specific tenant address
     * @param body     The tenant address to modify
     * @return The modified tenant address{@link AddressDto} object
     */
    @PutMapping(value = "/{guid}/addresses/{addrguid}", consumes = "application/vnd.softserve.address+json",
            produces = "application/vnd.softserve.address+json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AddressDto modifyTenantAddress(@PathVariable("guid") String guid, @PathVariable("addrguid") String addrGuid, @RequestBody String body) {
        logger.info("Accepted modified address of the tenant " + guid + " from the client:\n" + body);

        AddressDto addressDto = gson.fromJson(body, AddressDto.class);

        logger.info("Sending the modified address of the tenant " + guid + " to the client:\n" + gson.toJson(addressDto));
        return addressDto;
    }

    /**
     * Deletes the specific tenant property
     *
     * @param guid     tenant ID from the URN
     * @param addrGuid specific address ID from the URN
     */
    @DeleteMapping("/{guid}/addresses/{addrguid}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTenantAddress(@PathVariable("guid") String guid, @PathVariable("addrguid") String addrGuid) {
        logger.info("Accepted request to delete the address " + addrGuid + " ot the tenant " + guid);

        logger.info("the address " + addrGuid + " ot the tenant " + guid + " successfully deleted");
    }
}
