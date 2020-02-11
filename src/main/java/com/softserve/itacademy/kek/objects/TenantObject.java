package com.softserve.itacademy.kek.objects;

import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

/**
 * Object data level for collecting and transforming data from a database.
 * Collecting from {@link Tenant}, {@link TenantDetails}
 */
public class TenantObject {

    @NotNull
    private UUID guid;

    private String owner;

    @NotNull
    @Size(min = 1, max = 256)
    private String name;

    public TenantObject(@NotNull UUID guid,
                        String owner,
                        @NotNull @Size(min = 1, max = 256) String name,
                        DetailsObject tenantDetailsObject) {
        this.guid = guid;
        this.owner = owner;
        this.name = name;
        this.tenantDetailsObject = tenantDetailsObject;
    }

    private DetailsObject tenantDetailsObject;

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public DetailsObject getTenantDetailsObject() {
        return tenantDetailsObject;
    }

    public void setTenantDetailsObject(DetailsObject tenantDetailsObject) {
        this.tenantDetailsObject = tenantDetailsObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantObject that = (TenantObject) o;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(name, that.name) &&
                Objects.equals(tenantDetailsObject, that.tenantDetailsObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, owner, name, tenantDetailsObject);
    }

    @Override
    public String toString() {
        return "TenantObject{" +
                "guid=" + guid +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", tenantDetailsObject=" + tenantDetailsObject +
                '}';
    }
}
