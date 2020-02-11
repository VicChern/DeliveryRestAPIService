package com.softserve.itacademy.kek.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
     * @param body list of properties objects as a JSON
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
     * @param id       tenant ID from URL
     * @param propGuid ID of the tenant specific property
     * @return Specific tenant tenant property
     */
    @GetMapping("/{id}/properties/{propguid}")
    public ResponseEntity<String> getTenantProperty(@PathVariable("id") String id, @PathVariable("propguid") String propGuid) {
        logger.info("Sending the tenant's(" + id + ") specific property(" + propGuid + ") to the client");

        return ResponseEntity.ok(getJSON(id, "received"));
    }

    /**
     * Modifies the specific tenant property
     *
     * @param id       tenant ID from URN
     * @param propGuid ID of the specific tenant property
     * @param body     The tenant property to modify
     * @return The modified tenant property object
     */
    @PutMapping("/{id}/properties/{propguid}")
    public ResponseEntity<String> modifyTenantProperty(@PathVariable("id") String id, @PathVariable("propguid") String propGuid, @RequestBody String body) {
        logger.info("Sending the modified tenant's(" + id + ") property(" + propGuid + ") to the client");

        return ResponseEntity.ok(body);
    }

    /**
     * Deletes the specific tenant property
     *
     * @param id       tenant ID from the URN
     * @param propGuid address ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}/properties/{propguid}")
    public ResponseEntity<String> deleteTenantProperty(@PathVariable("id") String id, @PathVariable("propguid") String propGuid) {
        logger.info("Tenant's(" + id + ") address(" + propGuid + ") was successfully deleted");

        return ResponseEntity.ok(getJSON(id, "deleted"));
    }

    /**
     * Find addressees of the specific tenant
     *
     * @param id tenant ID from URN tenant property
     * @return Specific List of tenant addresses
     */
    @GetMapping("/{id}/addresses")
    public ResponseEntity<String> getTenantAddresses(@PathVariable String id) {
        logger.info("Sending the list of tenant's(" + id + ") addresses to the client");

        return ResponseEntity.ok(getJSON(id, "received"));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<String> addTenantAddresses(@PathVariable String id, @RequestBody String body) {
        logger.info("Sending the created tenant's(" + id + ") address to the client");

        return ResponseEntity.ok(body);
    }

    /**
     * Finds specific address of the specific tenant
     *
     * @param id       tenant ID from URN
     * @param addrGuid ID of the specific address
     * @return Specific tenant tenant property
     */
    @GetMapping("/{id}/addresses/{addrguid}")
    public ResponseEntity<String> getTenantAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid) {
        logger.info("Sending the specific tenant's(" + id + ") address(" + addrGuid + ") to the client");

        return ResponseEntity.ok(getJSON(id, "received"));
    }

    /**
     * Modifies the specific tenant address
     *
     * @param id       tenant ID from URN
     * @param addrGuid ID of the specific tenant address
     * @param body     The tenant address to modify
     * @return The modified tenant address object
     */
    @PutMapping("/{id}/addresses/{addrguid}")
    public ResponseEntity<String> modifyTenantAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid, @RequestBody String body) {
        logger.info("Sending the modified specific tenant's(" + id + ") address(" + addrGuid + ") to the client");

        return ResponseEntity.ok(body);
    }

    /**
     * Deletes the specific tenant property
     *
     * @param id       tenant ID from the URN
     * @param addrGuid specific address ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}/addresses/{addrguid}")
    public ResponseEntity<String> deleteTenantAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid) {
        logger.info("Tenant's(" + id + ") specific address(" + addrGuid + ") was successfully deleted");

        return ResponseEntity.ok(getJSON(addrGuid, "deleted"));
    }
}
