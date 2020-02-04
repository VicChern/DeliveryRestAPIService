package com.softserve.itacademy.kek.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tenants", produces = "application/json; charset=UTF-8")
public class TenantController extends DefaultController {

    // Build Response (stub, temporary method)
    private String getJSON(String id, String status) {
        JSONObject json = new JSONObject();
        json.put("tenantID", id);
        json.put("status", status);
        return json.toString();
    }


    /**
     * Get information about tenants
     *
     * @return information about tenants (JSON)
     */
    @GetMapping
    public ResponseEntity<String> getTenantList() {
        JSONObject json = new JSONObject();
        json.append("tenantID", "1").append("tenantID", "2").append("tenantID", "3");
        json.put("status", "received");
        return ResponseEntity.ok(json.toString());
    }

    /**
     * Creates a new tenant
     *
     * @return operation status as a JSON
     */
    @PostMapping
    public ResponseEntity<String> addTenant() {
        return ResponseEntity.ok(getJSON("new", "added"));
    }

    /**
     * Finds the specific tenant"
     *
     * @param id tenant id from the URN
     * @return tenant information as a JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getTenant(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "received"));
    }

    /**
     * Modifies information of the specified tenant
     *
     * @param id tenant ID from the URN
     * @return operation status as a JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifyTenant(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "Modified"));
    }

    /**
     * Removes the specified tenant
     *
     * @param id tenant ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTenant(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "deleted"));
    }

    /**
     * Find properties of the specific tenant
     *
     * @param id tenant ID from URN
     * @return List of tenant properties
     */
    @GetMapping("/{id}/properties")
    public ResponseEntity<String> getTenantProperties(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "List of tenant properties"));
    }

    /**
     * Add properties of the specific tenant
     *
     * @param id tenant ID from URN
     * @return List of added tenant properties
     */
    @PostMapping("/{id}/properties")
    public ResponseEntity<String> addTenantProperties(@PathVariable String id, @RequestBody String body) {
        return ResponseEntity.ok(getJSON("new", "added"));
    }

    /**
     * Finds specific property of the specific tenant
     *
     * @param id       tenant ID from URN
     * @param propGuid ID of the tenant specific property
     * @return Specific tenant tenant property
     */
    @GetMapping("/{id}/properties/{propguid}")
    public ResponseEntity<String> getTenantProperty(@PathVariable String id, @PathVariable String propGuid) {
        return ResponseEntity.ok(getJSON(id, "Specific tenant property"));
    }

    /**
     * Modifies the specific tenant property
     *
     * @param id       tenant ID from URN
     * @param propGuid ID of the specific tenant property
     * @param body     The tenant property to modify
     * @return The modified tenant property object
     */
    @PostMapping("/{id}/properties{propguid}")
    public ResponseEntity<String> modifyTenantProperty(@PathVariable String id, @PathVariable String propGuid, @RequestBody String body) {
        return ResponseEntity.ok(getJSON(propGuid, "Modified"));
    }

    /**
     * Deletes the specific tenant property
     *
     * @param id       tenant ID from the URN
     * @param propguid address ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}/properties/{propguid}")
    public ResponseEntity<String> deleteTenantProperty(@PathVariable String id, @PathVariable String propguid) {
        return ResponseEntity.ok(getJSON(id, "deleted"));
    }

    /**
     * Find addressees of the specific tenant
     *
     * @param id tenant ID from URNtenant tenant property
     * @return Specific List of tenant addresses
     */
    @GetMapping("/{id}/addresses")
    public ResponseEntity<String> getTenantAddresses(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "List of tenant addresses"));
    }

    /**
     * Adds a new addresses
     *
     * @param id   tenant ID from URN
     * @param body The tenant address to add
     * @return The modified tenant property object
     */
    @PostMapping("/{id}/addresses")
    public ResponseEntity<String> addTenantAddresses(@PathVariable String id, @RequestBody String body) {
        return ResponseEntity.ok(getJSON("new", "Added"));
    }

    /**
     * Finds specific address of the specific tenant
     *
     * @param id       tenant ID from URN
     * @param addrGuid ID of the specific address
     * @return Specific tenant tenant property
     */
    @GetMapping("/{id}/addresses/{propguid}")
    public ResponseEntity<String> getTenantAddress(@PathVariable String id, @PathVariable String addrGuid) {
        return ResponseEntity.ok(getJSON(id, "Specific tenant address"));
    }

    /**
     * Modifies the specific tenant address
     *
     * @param id       tenant ID from URN
     * @param addrGuid ID of the specific tenant address
     * @param body     The tenant address to modify
     * @return The modified tenant address object
     */
    @PostMapping("/{id}/addresses{propguid}")
    public ResponseEntity<String> modifyTenantAddress(@PathVariable String id, @PathVariable String addrGuid, @RequestBody String body) {
        return ResponseEntity.ok(getJSON(addrGuid, "Modified"));
    }

    /**
     * Deletes the specific tenant property
     *
     * @param id       tenant ID from the URN
     * @param addrGuid specific address ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}/addresses/{addrguid}")
    public ResponseEntity<String> deleteTenantAddress(@PathVariable String id, @PathVariable String addrGuid) {
        return ResponseEntity.ok(getJSON(addrGuid, "deleted"));
    }
}
