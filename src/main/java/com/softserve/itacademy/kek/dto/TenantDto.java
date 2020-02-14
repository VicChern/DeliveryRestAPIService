package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class TenantDto {

    @NotNull
    private String guid;
    private String owner;

    @NotNull
    @Max(256)
    private String name;
    private TenantDetailsDto details;

    public TenantDto() {
    }

    public TenantDto(String guid, String owner, String name, TenantDetailsDto details) {
        this.guid = guid;
        this.owner = owner;
        this.name = name;
        this.details = details;
    }

    public String getGuid() {
        return guid;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
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
