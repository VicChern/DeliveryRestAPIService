package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class TenantPropertiesDto {
    private String guid;
    private String tenant;
    private String type;

    @NotNull
    @Max(256)
    private String key;

    @NotNull
    @Max(1024)
    private String value;

    public TenantPropertiesDto() {
    }

    public TenantPropertiesDto(String guid, String tenant, String type, String key, String value) {
        this.guid = guid;
        this.tenant = tenant;
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getGuid() {
        return guid;
    }

    public String getTenant() {
        return tenant;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TenantPropertiesDTO{" +
                "guid='" + guid + '\'' +
                ", tenant='" + tenant + '\'' +
                ", type='" + type + '\'' +
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
                type.equals(that.type) &&
                key.equals(that.key) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, tenant, type, key, value);
    }
}
