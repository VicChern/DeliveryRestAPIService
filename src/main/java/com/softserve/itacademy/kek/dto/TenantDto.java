package com.softserve.itacademy.kek.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.ITenantDetails;
import com.softserve.itacademy.kek.models.IUser;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

public class TenantDto implements ITenant {

    private UUID guid;

    private UUID owner;

    @NotNull
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

    @Override
    public IUser getTenantOwner() {
        return new UserDto(owner, null, null, null, null, null);
    }

    @Override
    public ITenantDetails getTenantDetails() {
        return tenantDetails;
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
