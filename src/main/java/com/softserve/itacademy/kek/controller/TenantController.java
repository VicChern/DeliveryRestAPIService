package com.softserve.itacademy.kek.controller;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.AddressListDto;
import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.dto.TenantListDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesListDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.dto.UserListDto;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantDetails;
import com.softserve.itacademy.kek.services.ITenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/tenants")
public class TenantController extends DefaultController {
    final static Logger logger = LoggerFactory.getLogger(TenantController.class);

    private final ITenantService tenantService;

    @Autowired
    public TenantController(ITenantService userService) {
        this.tenantService = userService;
    }

    /**
     * Temporary method for TenantDto stub
     *
     * @return {@link TenantDto} stub
     */
    private TenantDto getTenantDtoStub() {
         TenantDetailsDto detailsDto = new TenantDetailsDto("some payload", "http://awesomepicture.com");
         TenantDto tenantDto = new TenantDto(UUID.fromString("guid12345qwawt"), "tenant", "Petro", detailsDto);
        return tenantDto;

    }

    private TenantDto transform(ITenant tenant) {
        TenantDetailsDto tenantDetailsDto = new TenantDetailsDto(tenant.getTenantDetails().getPayload(), tenant.getTenantDetails().getImageUrl());
        TenantDto tenantDto = new TenantDto(tenant.getGuid(), tenant.getName(), tenant.getTenantOwner(), tenantDetailsDto);
        return tenantDto;
    }

    /**
     * Temporary method for TenantPropertiesDto stub
     *
     * @return {@link TenantPropertiesDto} stub
     */
    private TenantPropertiesDto getTenantPropertiesDtoStub() {
        TenantPropertiesDto tenantPropertiesDto = new TenantPropertiesDto("guid12345qwawt", "glovo", "additional info", "workingDay", "Wednesday");
        return tenantPropertiesDto;
    }

    /**
     * Temporary method for AddressDto stub
     *
     * @return {@link AddressDto} stub
     */
    private AddressDto getAddressDtoStub() {
        AddressDto addressDto = new AddressDto("guid12345qwert", "alias", "Leipzigzskaya 15v", "Some notes...");
        return addressDto;
    }

    /**
     * Get information about tenants
     *
     * @return Response Entity with a list of {@link TenantDto} objects as a JSON
     */
    @GetMapping(produces = "application/vnd.softserve.tenantList+json")
    public ResponseEntity<TenantListDto> getTenantList() {
        logger.info("Client requested the list of all tenants");

        TenantListDto tenantList = new TenantListDto();
        tenantList.addTenant(getTenantDtoStub());

        logger.info("Sending list of all tenants to the client:\n{}", tenantList);
        return ResponseEntity
                .ok()
                .body(tenantList);
    }

    /**
     * Creates a new tenant
     *
     * @param body {@link TenantDto} object as a JSON
     * @return Response Entity with {@link TenantDto} object as a JSON
     */
    @PostMapping(consumes = "application/vnd.softserve.tenantList+json", produces = "application/vnd.softserve.tenantList+json")
    public ResponseEntity<TenantListDto> addTenant(@RequestBody @Valid TenantListDto body) {
        logger.info("Accepted requested to create a new tenant:\n{}", body);

        ITenant createdTenant = tenantService.create(body.getTenantList().get(0));
        TenantDto tenantDto = transform(createdTenant);

        body = new TenantListDto();
        body.addTenant(tenantDto);

        logger.info("Sending the created tenants to the client:\n" + body);
        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Finds the specific tenant
     *
     * @param guid tenant ID from the URL
     * @return Response Entity with {@link TenantDto} object as a JSON
     */
    @GetMapping(value = "/{guid}", produces = "application/vnd.softserve.tenant+json")
    public ResponseEntity<TenantDto> getTenant(@PathVariable String guid) {
        logger.info("Client requested the tenant {}", guid);

        TenantDto tenantDto = getTenantDtoStub();

        logger.info("Sending the specific tenant({}) to the client", guid);
        return ResponseEntity
                .ok()
                .body(tenantDto);
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
    public ResponseEntity<TenantDto> modifyTenant(@PathVariable String guid, @RequestBody @Valid TenantDto body) {
        logger.info("Accepted modified tenant from the client:\n{}", body);

        logger.info("Sending the modified tenant to the client:\n{}", body);
        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Removes the specified tenant
     *
     * @param guid tenant ID from the URL
     */
    @DeleteMapping("/{guid}")
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
    @GetMapping(value = "/{guid}/properties", produces = "application/vnd.softserve.tenantPropertyList+json")
    public ResponseEntity<TenantPropertiesListDto> getTenantProperties(@PathVariable String guid) {
        logger.info("Client requested all the properties of the tenant {}", guid);

        TenantPropertiesListDto tenantPropertiesList = new TenantPropertiesListDto();
        tenantPropertiesList.addProperties(getTenantPropertiesDtoStub());

        logger.info("Sending the list of tenant's({}) properties to the client:\n{}", tenantPropertiesList, guid);
        return ResponseEntity
                .ok()
                .body(tenantPropertiesList);
    }

    /**
     * Add new properties of the specific tenant
     *
     * @param guid tenant ID from URL
     * @param body property object as a JSON
     * @return list of the {@link TenantPropertiesDto} objects as a JSON
     */
    @PostMapping(value = "/{guid}/properties", consumes = "application/vnd.softserve.tenantPropertyList+json",
            produces = "application/vnd.softserve.tenantPropertyList+json")
    public ResponseEntity<TenantPropertiesListDto> addTenantProperties(@PathVariable String guid,
                                                                       @RequestBody @Valid TenantPropertiesListDto body) {
        logger.info("Accepted requested to create a new properties for tenant:{}}:\n{}", guid, body);

        logger.info("Sending the created tenant's({}) properties to the client", body);
        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Finds specific property of the specific tenant
     *
     * @param guid     tenant ID from URL
     * @param propGuid ID of the tenant specific property
     * @return Response entity with a specific tenant property {@link TenantPropertiesDto}
     */
    @GetMapping(value = "/{guid}/properties/{propguid}", produces = "application/vnd.softserve.tenantProperty+json")
    public ResponseEntity<TenantPropertiesDto> getTenantProperty(@PathVariable("guid") String guid, @PathVariable("propguid") String propGuid) {
        logger.info("Sending the tenant's({}) specific property({}) to the client", guid, propGuid);

        TenantPropertiesDto tenantPropertiesDto = getTenantPropertiesDtoStub();

        logger.info("Sending specific property of the tenant {} to the client:\n{}", guid, tenantPropertiesDto);
        return ResponseEntity
                .ok()
                .body(tenantPropertiesDto);
    }

    /**
     * Modifies the specific tenant property
     *
     * @param guid     tenant ID from URN
     * @param propGuid ID of the specific tenant property
     * @param body     The tenant property to modify
     * @return Response entity with modified tenant property object{@link TenantPropertiesDto}
     */
    @PutMapping(value = "/{guid}/properties/{propguid}", consumes = "application/vnd.softserve.tenantProperty+json",
            produces = "application/vnd.softserve.tenantproperty+json")
    public ResponseEntity<TenantPropertiesDto> modifyTenantProperty(@PathVariable("guid") String guid,
                                                                    @PathVariable("propguid") String propGuid,
                                                                    @RequestBody @Valid TenantPropertiesDto body) {
        logger.info("Sending the modified tenant's({}) property({}) to the client", guid, propGuid);

        logger.info("Sending the modified property of the tenant {} to the client:\n{}", guid, body);

        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Deletes the specific tenant property
     *
     * @param guid     tenant ID from the URN
     * @param propGuid address ID from the URN
     */
    @DeleteMapping("/{guid}/properties/{propguid}")
    public ResponseEntity deleteTenantProperty(@PathVariable("guid") String guid, @PathVariable("propguid") String propGuid) {
        logger.info("Accepted request to delete the property {} ot the tenant {}", propGuid, guid);

        logger.info("the property {} ot the tenant {} successfully deleted", propGuid, guid);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Find addressees of the specific tenant
     *
     * @param guid tenant ID from URN tenant address
     * @return Response entity with a list of tenant addresses{@link AddressDto}
     */
    @GetMapping(value = "/{guid}/addresses", produces = "application/vnd.softserve.addressList+json")
    public ResponseEntity<AddressListDto> getTenantAddresses(@PathVariable String guid) {
        logger.info("Client requested all the addresses {}", guid);

        AddressListDto addressesList = new AddressListDto();
        addressesList.addAddress(getAddressDtoStub());

        logger.info("Sending the list of addresses of the tenant {} to the client:\n{}", guid, addressesList);
        return ResponseEntity
                .ok()
                .body(addressesList);
    }

    /**
     * Adds a new address for the specific tenant
     *
     * @param guid tenant ID from the URN
     * @param body address object as a JSON
     * @return Response entity with a {@link AddressDto} object as a JSON
     */
    @PostMapping(value = "/{guid}/addresses", consumes = "application/vnd.softserve.addressList+json",
            produces = "application/vnd.softserve.addressList+json")
    @ResponseStatus()
    public ResponseEntity<AddressListDto> addTenantAddresses(@PathVariable String guid, @RequestBody @Valid AddressListDto body) {
        logger.info("Accepted requested to create a new addresses for user:" + guid + ":\n" + body);

        logger.info("Sending the created users's addresses to the client:\n" + body);
        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Finds specific address of the specific tenant
     *
     * @param guid     tenant ID from URN
     * @param addrGuid ID of the specific address
     * @return Response Entity with a specific tenant tenant property{@link TenantPropertiesDto}
     */
    @GetMapping(value = "/{guid}/addresses/{addrguid}", produces = "application/vnd.softserve.address+json")
    public ResponseEntity<AddressDto> getTenantAddress(@PathVariable("guid") String guid, @PathVariable("addrguid") String addrGuid) {
        logger.info("Client requested the address {} of the tenant {}", addrGuid, guid);

        AddressDto addressDto = getAddressDtoStub();

        logger.info("Sending the address of the tenant {} to the client:\n{}", guid, addressDto);
        return ResponseEntity.ok().body(addressDto);
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
    public ResponseEntity<AddressDto> modifyTenantAddress(@PathVariable("guid") String guid,
                                                          @PathVariable("addrguid") String addrGuid,
                                                          @RequestBody @Valid AddressDto body) {
        logger.info("Accepted modified address of the tenant {} from the client:\n{}", guid, body);

        logger.info("Sending the modified address of the tenant {} to the client:\n{}", guid, body);
        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Deletes the specific tenant property
     *
     * @param guid     tenant ID from the URN
     * @param addrGuid specific address ID from the URN
     */
    @DeleteMapping("/{guid}/addresses/{addrguid}")
    public ResponseEntity deleteTenantAddress(@PathVariable("guid") String guid, @PathVariable("addrguid") String addrGuid) {
        logger.info("Accepted request to delete the address {} ot the tenant {}", addrGuid, guid);

        logger.info("the address {} ot the tenant {} successfully deleted", addrGuid, guid);

        return new ResponseEntity(HttpStatus.OK);
    }
}
