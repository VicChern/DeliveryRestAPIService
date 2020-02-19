package com.softserve.itacademy.kek.dto;

import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.ITenantDetails;
import com.softserve.itacademy.kek.models.IUser;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

public class TenantDto implements ITenant {

    @NotNull
    private UUID guid;
    private String tenantOwner;

    @NotNull
    @Size(max = 256)
    private String name;
    private ITenantDetails details;

    public TenantDto() {
    }

    public TenantDto(UUID guid, String owner, String name, ITenantDetails details) {
        this.guid = guid;
        this.tenantOwner = owner;
        this.name = name;
        this.details = details;
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
    public String getTenantOwner() {
        return tenantOwner;
    }

    @Override
    public ITenantDetails getTenantDetails() {
        return details;
    }
    @Override
    public String toString() {
        return "TenantDto{" +
                "guid='" + guid + '\'' +
                ", owner='" + tenantOwner + '\'' +
                ", name='" + name + '\'' +
                ", details=" + details +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantDto tenantDto = (TenantDto) o;
        return Objects.equals(guid, tenantDto.guid) &&
                Objects.equals(tenantOwner, tenantDto.tenantOwner) &&
                Objects.equals(name, tenantDto.name) &&
                Objects.equals(details, tenantDto.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, tenantOwner, name, details);
    }
}
