package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.ListWrapperDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import com.softserve.itacademy.kek.mappers.IAddressMapper;
import com.softserve.itacademy.kek.mappers.ITenantMapper;
import com.softserve.itacademy.kek.mappers.ITenantPropertiesMapper;
import com.softserve.itacademy.kek.models.IAddress;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.ITenantProperties;
import com.softserve.itacademy.kek.services.IAddressService;
import com.softserve.itacademy.kek.services.ITenantPropertiesService;
import com.softserve.itacademy.kek.services.ITenantService;


@RestController
@RequestMapping(path = "/tenants")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TenantController extends DefaultController {
    private final static Logger logger = LoggerFactory.getLogger(TenantController.class);

    private final ITenantService tenantService;
    private final ITenantPropertiesService tenantPropertiesService;
    private final IAddressService addressService;

    @Autowired
    public TenantController(ITenantService tenantService, ITenantPropertiesService tenantPropertiesService, IAddressService addressService) {
        this.tenantService = tenantService;
        this.tenantPropertiesService = tenantPropertiesService;
        this.addressService = addressService;
    }

    /**
     * Get information about tenants
     *
     * @return Response Entity with a list of {@link TenantDto} objects as a JSON
     */
    @GetMapping(produces = KekMediaType.TENANT_LIST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ListWrapperDto<TenantDto>> getTenantList() {
        logger.info("Client requested the list of all tenants");

        List<ITenant> tenantList = tenantService.getAll();
        ListWrapperDto<TenantDto> tenantListDto = new ListWrapperDto<>(tenantList
                .stream()
                .map(ITenantMapper.INSTANCE::toTenantDto)
                .collect(Collectors.toList()));

        logger.info("Sending list of all tenants to the client:\n{}", tenantList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tenantListDto);
    }

    /**
     * Creates a new tenant
     *
     * @param tenantDto {@link TenantDto} object as a JSON
     * @return Response Entity with {@link TenantDto} object as a JSON
     */
    @PostMapping(consumes = KekMediaType.TENANT, produces = KekMediaType.TENANT)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantDto> addTenant(@RequestBody @Valid TenantDto tenantDto) {
        logger.info("Accepted requested to create a new tenant:\n{}", tenantDto);

        ITenant iTenant = tenantService.create(tenantDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ITenantMapper.INSTANCE.toTenantDto(iTenant));
    }

    /**
     * Finds the specific tenant
     *
     * @param guid tenant ID from the URL
     * @return Response Entity with {@link TenantDto} object as a JSON
     */
    @GetMapping(value = KekMappingValues.GUID, produces = KekMediaType.TENANT)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantDto> getTenant(@PathVariable String guid) {
        logger.info("Client requested the tenant {}", guid);

        ITenant tenant = tenantService.getByGuid(UUID.fromString(guid));
        TenantDto tenantDto = ITenantMapper.INSTANCE.toTenantDto(tenant);

        logger.info("Sending the specific tenant({}) to the client", guid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tenantDto);
    }


    /**
     * Modifies information of the specified tenant
     *
     * @param guid   tenant ID from the URL
     * @param tenant tenant object
     * @return Response Entity with {@link TenantDto} object as a JSON
     */
    @PutMapping(value = KekMappingValues.GUID, consumes = KekMediaType.TENANT,
            produces = KekMediaType.TENANT)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantDto> modifyTenant(@PathVariable String guid, @RequestBody @Valid TenantDto tenant) {
        logger.info("Accepted current tenant from the client:\n{}", tenant);

        ITenant modifiedTenant = tenantService.update(tenant, UUID.fromString(guid));
        TenantDto modifiedTenantDto = ITenantMapper.INSTANCE.toTenantDto(modifiedTenant);

        logger.info("Sending the modified tenant to the client:\n{}", tenant);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(modifiedTenantDto);
    }

    /**
     * Removes the specified tenant
     *
     * @param guid tenant ID from the URL
     */
    @DeleteMapping(KekMappingValues.GUID)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity deleteTenant(@PathVariable String guid) {
        logger.info("Accepted request to delete the tenant {}", guid);

        tenantService.deleteByGuid(UUID.fromString(guid));

        logger.info("Tenant({}) was successfully deleted", guid);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

    /**
     * Find properties of the specific tenant
     *
     * @param guid tenant ID from URL
     * @return Response Entity with a List of {@link TenantPropertiesDto} objects as a JSON
     */
    @GetMapping(value = KekMappingValues.PROPERTIES, produces = KekMediaType.TENANT_PROPERTY)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ListWrapperDto<TenantPropertiesDto>> getTenantProperties(@PathVariable String guid) {
        logger.info("Client requested all the properties of the tenant {}", guid);

        List<ITenantProperties> tenantProperties = tenantPropertiesService.getAllForTenant(UUID.fromString(guid));
        ListWrapperDto<TenantPropertiesDto> tenantPropertiesListDto = new ListWrapperDto<>(tenantProperties
                .stream()
                .map(ITenantPropertiesMapper.INSTANCE::toTenantPropertiesDto)
                .collect(Collectors.toList()));

        logger.info("Sending the list of tenant's({}) properties to the client:\n{}", tenantPropertiesListDto, guid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tenantPropertiesListDto);
    }

    /**
     * Add new properties of the specific tenant
     *
     * @param guid                    tenant ID from URL
     * @param tenantPropertiesListDto list of {@link TenantPropertiesDto} objects as a JSON
     * @return Response entity with a list of {@link TenantPropertiesDto} objects as a JSON
     */
    @PostMapping(value = KekMappingValues.PROPERTIES, consumes = KekMediaType.TENANT_PROPERTY_LIST,
            produces = KekMediaType.TENANT_PROPERTY_LIST)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ListWrapperDto<TenantPropertiesDto>> addTenantProperties(@PathVariable String guid,
                                                                                   @RequestBody ListWrapperDto<TenantPropertiesDto> tenantPropertiesListDto) {
        logger.info("Accepted requested to create a new properties for tenant:{}}:\n{}", guid, tenantPropertiesListDto);

        List<ITenantProperties> propertiesList = new LinkedList<>(tenantPropertiesListDto.getList());
        List<ITenantProperties> tenantProperties = tenantPropertiesService.create(propertiesList,
                UUID.fromString(guid));

        List<TenantPropertiesDto> tenantPropertiesDto = tenantProperties
                .stream()
                .map(ITenantPropertiesMapper.INSTANCE::toTenantPropertiesDto)
                .collect(Collectors.toList());
        ListWrapperDto<TenantPropertiesDto> tenantPropertiesList = new ListWrapperDto<>(tenantPropertiesDto);

        logger.info("Sending the created tenant's({}) properties to the client", tenantProperties);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tenantPropertiesList);
    }

    /**
     * Finds specific property of the specific tenant
     *
     * @param guid     tenant ID from URL
     * @param propGuid ID of the tenant specific property
     * @return Response entity with a specific tenant property {@link TenantPropertiesDto}
     */
    @GetMapping(value = KekMappingValues.PROP_GUID, produces = KekMediaType.TENANT_PROPERTY)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantPropertiesDto> getTenantProperty(@PathVariable("guid") String guid, @PathVariable("propguid") String propGuid) {
        logger.info("Sending the tenant's({}) specific property({}) to the client", guid, propGuid);

        ITenantProperties tenantProperties = tenantPropertiesService.getPropertyByTenantGuid(UUID.fromString(guid), UUID.fromString(propGuid));
        TenantPropertiesDto tenantPropertiesDto = ITenantPropertiesMapper.INSTANCE.toTenantPropertiesDto(tenantProperties);

        logger.info("Sending specific property of the tenant {} to the client:\n{}", guid, tenantPropertiesDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tenantPropertiesDto);
    }

    /**
     * Modifies the specific tenant property
     *
     * @param guid                tenant ID from URN
     * @param propGuid            ID of the specific tenant property
     * @param tenantPropertiesDto The tenant property to modify
     * @return Response entity with modified tenant property object{@link TenantPropertiesDto}
     */
    @PutMapping(value = KekMappingValues.PROP_GUID, consumes = KekMediaType.TENANT_PROPERTY,
            produces = KekMediaType.TENANT_PROPERTY)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<TenantPropertiesDto> modifyTenantProperty(@PathVariable("guid") String guid,
                                                                    @PathVariable("propguid") String propGuid,
                                                                    @RequestBody @Valid TenantPropertiesDto tenantPropertiesDto) {
        logger.info("Sending the modified tenant's({}) property({}) to the client", guid, propGuid);

        ITenantProperties modifiedTenant = tenantPropertiesService.update(UUID.fromString(guid), UUID.fromString(propGuid), tenantPropertiesDto);
        TenantPropertiesDto modifiedTenantDto = ITenantPropertiesMapper.INSTANCE.toTenantPropertiesDto(modifiedTenant);

        logger.info("Sending the modified property of the tenant {} to the client:\n{}", guid, tenantPropertiesDto);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(modifiedTenantDto);
    }

    /**
     * Deletes the specific tenant property
     *
     * @param guid     tenant ID from the URN
     * @param propGuid address ID from the URN
     */
    @DeleteMapping(KekMappingValues.PROP_GUID)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity deleteTenantProperty(@PathVariable("guid") String guid, @PathVariable("propguid") String propGuid) {
        logger.info("Accepted request to delete the property {} ot the tenant {}", propGuid, guid);

        tenantPropertiesService.delete(UUID.fromString(guid), UUID.fromString(propGuid));

        logger.info("the property {} ot the tenant {} successfully deleted", propGuid, guid);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

    /**
     * Find addressees of the specific tenant
     *
     * @param guid tenant ID from URN tenant property
     * @return Response entity with a list of {@link AddressDto} objects as a JSON
     */
    @GetMapping(value = KekMappingValues.ADDRESSES, produces = KekMediaType.ADDRESS_LIST)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ListWrapperDto<AddressDto>> getTenantAddresses(@PathVariable String guid) {
        logger.info("Client requested all the addresses {}", guid);

        List<IAddress> addresses = addressService.getAllForTenant(UUID.fromString(guid));
        ListWrapperDto<AddressDto> addressListDto = new ListWrapperDto<>(addresses
                .stream()
                .map(IAddressMapper.INSTANCE::toAddressDto)
                .collect(Collectors.toList()));

        logger.info("Sending the list of addresses of the tenant {} to the client:\n{}", guid, addressListDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressListDto);
    }

    /**
     * Adds a new address for the specific tenant
     *
     * @param guid            tenant ID from the URN
     * @param newAddressesDto object with a list of {@link AddressDto} as a JSON
     * @return Response entity with a list of {@link AddressDto} objects as a JSON
     */
    @PostMapping(value = KekMappingValues.ADDRESSES, consumes = KekMediaType.ADDRESS,
            produces = KekMediaType.ADDRESS)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ListWrapperDto<AddressDto>> addTenantAddresses(@PathVariable String guid,
                                                                         @RequestBody @Valid ListWrapperDto<AddressDto> newAddressesDto) {
        logger.info("Accepted requested to create a new addresses for tenant:{}:\n", newAddressesDto);
        ListWrapperDto<AddressDto> createdAddresses = new ListWrapperDto<>();

        for (AddressDto newAddress : newAddressesDto.getList()) {
            IAddress createdAddress = addressService.createForTenant(newAddress, UUID.fromString(guid));
            AddressDto addressDto = IAddressMapper.INSTANCE.toAddressDto(createdAddress);

            createdAddresses.addKekItem(addressDto);
        }

        logger.info("Sending the created tenant's addresses to the client:\n{}", createdAddresses);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdAddresses);
    }

    /**
     * Finds specific address of the specific tenant
     *
     * @param guid     tenant ID from URN
     * @param addrGuid ID of the specific address
     * @return Response Entity with a specific tenant tenant property{@link TenantPropertiesDto}
     */
    @GetMapping(value = KekMappingValues.ADDR_GUID, produces = KekMediaType.ADDRESS)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<AddressDto> getTenantAddress(@PathVariable("guid") String guid, @PathVariable("addrguid") String addrGuid) {
        logger.info("Client requested the address {} of the tenant {}", addrGuid, guid);

        IAddress address = addressService.getForTenant(UUID.fromString(addrGuid), UUID.fromString(guid));
        AddressDto addressDto = IAddressMapper.INSTANCE.toAddressDto(address);

        logger.info("Sending the address of the tenant {} to the client:\n{}", guid, addressDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressDto);
    }

    /**
     * Modifies the specific tenant address
     *
     * @param guid             tenant ID from URN
     * @param addrGuid         ID of the specific tenant address
     * @param tenantAddressDto The tenant address to modify
     * @return Response entity with modified tenant address{@link AddressDto} object
     */
    @PutMapping(value = KekMappingValues.ADDR_GUID, consumes = KekMediaType.ADDRESS,
            produces = KekMediaType.ADDRESS)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<AddressDto> modifyTenantAddress(@PathVariable("guid") String guid,
                                                          @PathVariable("addrguid") String addrGuid,
                                                          @RequestBody @Valid AddressDto tenantAddressDto) {
        logger.info("Accepted modified address of the tenant {} from the client:\n{}", guid, tenantAddressDto);

        IAddress modifiedAddress = addressService.updateForTenant(tenantAddressDto, UUID.fromString(guid), UUID.fromString(addrGuid));
        AddressDto modifiedAddressDto = IAddressMapper.INSTANCE.toAddressDto(modifiedAddress);

        logger.info("Sending the modified address of the tenant {} to the client:\n{}", guid, tenantAddressDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modifiedAddressDto);
    }

    /**
     * Deletes the specific tenant property
     *
     * @param guid     tenant ID from the URN
     * @param addrGuid specific address ID from the URN
     */
    @DeleteMapping(KekMappingValues.ADDR_GUID)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity deleteTenantAddress(@PathVariable("guid") String guid, @PathVariable("addrguid") String addrGuid) {
        logger.info("Accepted request to delete the address {} ot the tenant {}", addrGuid, guid);

        addressService.deleteForTenant(UUID.fromString(addrGuid), UUID.fromString(guid));

        logger.info("the address {} ot the tenant {} successfully deleted", addrGuid, guid);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }
}
