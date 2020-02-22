package com.softserve.itacademy.kek.dto;

import com.softserve.itacademy.kek.models.IPropertyType;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.ITenantProperties;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

public class TenantPropertiesDto implements ITenantProperties {
    private UUID guid;
    private ITenant tenant;
    private IPropertyType propertyType;

    @NotNull
    @Size(max = 256)
    private String key;

    @NotNull
    @Size(max = 1024)
    private String value;

    public TenantPropertiesDto() {
    }

    public TenantPropertiesDto(UUID guid, ITenant tenant, IPropertyType type, String key, String value) {
        this.guid = guid;
        this.tenant = tenant;
        this.propertyType = type;
        this.key = key;
        this.value = value;
    }

    @Override
    public UUID getGuid() {
        return guid;
    }

    @Override
    public ITenant getTenant() {
        return tenant;
    }

    @Override
    public IPropertyType getPropertyType() {
        return propertyType;
    }


    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TenantPropertiesDTO{" +
                "guid='" + guid + '\'' +
                ", tenant='" + tenant + '\'' +
                ", type='" + propertyType + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantPropertiesDto)) return false;
        TenantPropertiesDto that = (TenantPropertiesDto) o;
        return guid.equals(that.guid) &&
                tenant.equals(that.tenant) &&
                propertyType.equals(that.propertyType) &&
                key.equals(that.key) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, tenant, propertyType, key, value);
    }
}
