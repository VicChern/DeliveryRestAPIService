package com.softserve.itacademy.kek.dto;

import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.ITenantDetails;
import com.softserve.itacademy.kek.models.IUser;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

public class TenantDto implements ITenant {

    private UUID guid;
    private String owner;

    @NotNull
    @Size(max = 256)
    private String name;
    private TenantDetailsDto details;
    private ITenantDetails tenantDetails;
    private IUser tenantOwner;

    public TenantDto() {
    }

    public TenantDto(UUID guid, String owner, String name, TenantDetailsDto details) {
        this.guid = guid;
        this.owner = owner;
        this.name = name;
        this.details = details;
    }

    public UUID getGuid() {
        return guid;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    @Override
    public IUser getTenantOwner() {
        return new UserDto(UUID.fromString(owner),null,null,null,null,null);
    }

    @Override
    public ITenantDetails getTenantDetails() {
        return tenantDetails;
    }

    public TenantDetailsDto getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "TenantDto{" +
                "guid='" + guid + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", details=" + details +
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
                details.equals(tenantDto.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, owner, name, details);
    }
}
