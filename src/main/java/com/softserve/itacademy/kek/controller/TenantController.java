package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;

@RestController
@RequestMapping(path = "/tenants")
public class TenantController extends DefaultController {
    final static Logger logger = LoggerFactory.getLogger(TenantController.class);

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
     * @return Response Entity with a list of {@link TenantDto} objects as a JSON
     */
    @GetMapping(produces = "application/vnd.softserve.tenant+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<List<TenantDto>> getTenantList() {
        logger.info("Client requested the list of all tenants");

        List<TenantDto> tenantList = new ArrayList<>();
        tenantList.add(getTenantDtoStub());

        logger.info("Sending list of all tenants to the client:\n{}", tenantList);
        return new ResponseEntity<>(tenantList, HttpStatus.OK);
    }

    /**
     * Creates a new tenant
     *
     * @param body {@link TenantDto} object as a JSON
     * @return Response Entity with {@link TenantDto} object as a JSON
     */
    @PostMapping(consumes = "application/vnd.softserve.tenant+json", produces = "application/vnd.softserve.tenant+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantDto> addTenant(@RequestBody @Valid TenantDto body) {
        logger.info("Accepted requested to create a new tenant:\n{}", body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Finds the specific tenant
     *
     * @param guid tenant ID from the URL
     * @return Response Entity with {@link TenantDto} object as a JSON
     */
    @GetMapping(value = "/{guid}", produces = "application/vnd.softserve.tenant+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantDto> getTenant(@PathVariable String guid) {
        logger.info("Client requested the tenant {}", guid);

        TenantDto tenantDto = getTenantDtoStub();

        logger.info("Sending the specific tenant({}) to the client", guid);
        return new ResponseEntity<>(tenantDto, HttpStatus.OK);
    }


    /**
     * Modifies information of the specified tenant
     *
     * @param guid tenant ID from the URL
     * @param body tenant object
     * @return Response Entity with {@link TenantDto} object as a JSON
     */
    @PutMapping(value = "/{guid}", consumes = "application/vnd.softserve.tenant+json",
            produces = "application/vnd.softserve.tenant+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantDto> modifyTenant(@PathVariable String guid, @RequestBody @Valid TenantDto body) {
        logger.info("Accepted modified tenant from the client:\n{}", body);
        logger.info("Sending the modified tenant to the client:\n{}", body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Removes the specified tenant
     *
     * @param guid tenant ID from the URL
     */
    @DeleteMapping("/{guid}")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity deleteTenant(@PathVariable String guid) {
        logger.info("Accepted request to delete the tenant {}", guid);
        logger.info("Tenant({}}) was successfully deleted", guid);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Find properties of the specific tenant
     *
     * @param guid tenant ID from URL
     * @return Response Entity with a List of (@link TenantPropertiesDTO) objects as a JSON
     */
    @GetMapping(value = "/{guid}/properties", produces = "application/vnd.softserve.tenantproperty+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<List<TenantPropertiesDto>> getTenantProperties(@PathVariable String guid) {
        logger.info("Client requested all the properties of the tenant {}", guid);

        List<TenantPropertiesDto> tenantPropertiesList = new ArrayList<>();
        tenantPropertiesList.add(getTenantPropertiesDtoStub());

        logger.info("Sending the list of tenant's({}) properties to the client:\n{}", tenantPropertiesList, guid);

        return new ResponseEntity<>(tenantPropertiesList, HttpStatus.OK);
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
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantPropertiesDto> addTenantProperties(@PathVariable String guid,
                                                                   @RequestBody @Valid TenantPropertiesDto body) {
        logger.info("Accepted requested to create a new properties for tenant:{}}:\n{}", guid, body);
        logger.info("Sending the created tenant's({}) properties to the client", body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Finds specific property of the specific tenant
     *
     * @param guid     tenant ID from URL
     * @param propGuid ID of the tenant specific property
     * @return Response entity with a specific tenant property {@link TenantPropertiesDto}
     */
    @GetMapping(value = "/{guid}/properties/{propguid}", produces = "application/vnd.softserve.tenantproperty+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantPropertiesDto> getTenantProperty(@PathVariable("guid") String guid, @PathVariable("propguid") String propGuid) {
        logger.info("Sending the tenant's({}) specific property({}) to the client", guid, propGuid);

        TenantPropertiesDto tenantPropertiesDto = getTenantPropertiesDtoStub();

        logger.info("Sending specific property of the tenant {} to the client:\n{}", guid, tenantPropertiesDto);
        return new ResponseEntity<>(tenantPropertiesDto, HttpStatus.OK);
    }

    /**
     * Modifies the specific tenant property
     *
     * @param guid     tenant ID from URN
     * @param propGuid ID of the specific tenant property
     * @param body     The tenant property to modify
     * @return Response entity with modified tenant property object{@link TenantPropertiesDto}
     */
    @PutMapping(value = "/{guid}/properties/{propguid}", consumes = "application/vnd.softserve.tenantproperty+json",
            produces = "application/vnd.softserve.tenantproperty+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantPropertiesDto> modifyTenantProperty(@PathVariable("guid") String guid,
                                                                    @PathVariable("propguid") String propGuid,
                                                                    @RequestBody @Valid TenantPropertiesDto body) {
        logger.info("Sending the modified tenant's({}) property({}) to the client", guid, propGuid);
        logger.info("Sending the modified property of the tenant {} to the client:\n{}", guid, body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Deletes the specific tenant property
     *
     * @param guid     tenant ID from the URN
     * @param propGuid address ID from the URN
     */
    @DeleteMapping("/{guid}/properties/{propguid}")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity deleteTenantProperty(@PathVariable("guid") String guid, @PathVariable("propguid") String propGuid) {
        logger.info("Accepted request to delete the property {} ot the tenant {}", propGuid, guid);
        logger.info("the property {} ot the tenant {} successfully deleted", propGuid, guid);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Find addressees of the specific tenant
     *
     * @param guid tenant ID from URN tenant property
     * @return Response entity with a list of tenant addresses{@link AddressDto}
     */
    @GetMapping(value = "/{guid}/addresses", produces = "application/vnd.softserve.address+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<List<AddressDto>> getTenantAddresses(@PathVariable String guid) {
        logger.info("Client requested all the addresses {}", guid);

        List<AddressDto> addressesList = new ArrayList<>();
        addressesList.add(getTenantAddressDtoStub());

        logger.info("Sending the list of addresses of the tenant {} to the client:\n{}", guid, addressesList);
        return new ResponseEntity<>(addressesList, HttpStatus.OK);
    }

    /**
     * Adds a new address for the specific tenant
     *
     * @param guid tenant ID from the URN
     * @param body address object as a JSON
     * @return Response entity with a {@link AddressDto} object as a JSON
     */
    @PostMapping(value = "/{guid}/addresses", consumes = "application/vnd.softserve.address+json",
            produces = "application/vnd.softserve.address+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<AddressDto> addTenantAddresses(@PathVariable String guid, @RequestBody @Valid AddressDto body) {
        logger.info("Accepted requested to create a new address for tenant: {} :\n{}", guid, body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Finds specific address of the specific tenant
     *
     * @param guid     tenant ID from URN
     * @param addrGuid ID of the specific address
     * @return Response Entity with a specific tenant tenant property{@link TenantPropertiesDto}
     */
    @GetMapping(value = "/{guid}/addresses/{addrguid}", produces = "application/vnd.softserve.address+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<AddressDto> getTenantAddress(@PathVariable("guid") String guid, @PathVariable("addrguid") String addrGuid) {
        logger.info("Client requested the address {} of the tenant {}", addrGuid, guid);

        AddressDto addressDto = getTenantAddressDtoStub();

        logger.info("Sending the address of the tenant {} to the client:\n{}", guid, addressDto);
        return new ResponseEntity<>(addressDto, HttpStatus.OK);
    }

    /**
     * Modifies the specific tenant address
     *
     * @param guid     tenant ID from URN
     * @param addrGuid ID of the specific tenant address
     * @param body     The tenant address to modify
     * @return Response entity with modified tenant address{@link AddressDto} object
     */
    @PutMapping(value = "/{guid}/addresses/{addrguid}", consumes = "application/vnd.softserve.address+json",
            produces = "application/vnd.softserve.address+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<AddressDto> modifyTenantAddress(@PathVariable("guid") String guid,
                                                          @PathVariable("addrguid") String addrGuid,
                                                          @RequestBody @Valid AddressDto body) {
        logger.info("Accepted modified address of the tenant {} from the client:\n{}", guid, body);
        logger.info("Sending the modified address of the tenant {} to the client:\n{}", guid, body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Deletes the specific tenant property
     *
     * @param guid     tenant ID from the URN
     * @param addrGuid specific address ID from the URN
     */
    @DeleteMapping("/{guid}/addresses/{addrguid}")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity deleteTenantAddress(@PathVariable("guid") String guid, @PathVariable("addrguid") String addrGuid) {
        logger.info("Accepted request to delete the address {} ot the tenant {}", addrGuid, guid);
        logger.info("the address {} ot the tenant {} successfully deleted", addrGuid, guid);

        return new ResponseEntity(HttpStatus.OK);
    }
}
