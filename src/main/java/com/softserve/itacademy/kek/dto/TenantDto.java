package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.Size;
import java.beans.Transient;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.ITenantDetails;

public class TenantDto implements ITenant {

    private UUID guid;
    private UUID owner;

    @Size(max = 256)
    private String name;

    @JsonProperty("details")
    private TenantDetailsDto tenantDetails;

    public TenantDto() {
    }

    public TenantDto(UUID guid, UUID owner, String name, TenantDetailsDto details) {
        this.guid = guid;
        this.owner = owner;
        this.name = name;
        this.tenantDetails = details;
    }

    @Override
    public UUID getGuid() {
        return guid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Transient
    @Override
    public UserDto getTenantOwner() {
        return new UserDto(owner, null, null, null, null, null);
    }

    @Override
    public ITenantDetails getTenantDetails() {
        return tenantDetails;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTenantDetails(TenantDetailsDto tenantDetails) {
        this.tenantDetails = tenantDetails;
    }

    @Override
    public String toString() {
        return "TenantDto{" +
                "guid='" + guid + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", details=" + tenantDetails +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantDto)) return false;
        TenantDto tenantDto = (TenantDto) o;
        return guid.equals(tenantDto.guid) &&
                owner.equals(tenantDto.owner) &&
                name.equals(tenantDto.name) &&
                tenantDetails.equals(tenantDto.tenantDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, owner, name, tenantDetails);
    }
}
