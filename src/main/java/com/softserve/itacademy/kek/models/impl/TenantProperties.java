package com.softserve.itacademy.kek.models.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.softserve.itacademy.kek.models.IPropertyType;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.ITenantProperties;

@Entity
@Table(name = "obj_tenant_properties")
public class TenantProperties extends AbstractEntity implements ITenantProperties, Serializable {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_tenant")
    Tenant tenant;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_property")
    private Long idProperty;

    @NotNull
    @Column(name = "guid", unique = true)
    private UUID guid;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id_Property_Type")
    private PropertyType propertyType;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "key", unique = true, length = 256)
    private String key;

    @NotNull
    @Size(min = 1, max = 4096)
    @Column(name = "value", length = 4096)
    private String value;

    public Long getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(Long idProperty) {
        this.idProperty = idProperty;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ITenant getTenant() {
        return tenant;
    }

    public void setTenant(ITenant tenant) {
        this.tenant = (Tenant) tenant;
    }

    public IPropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantProperties)) return false;
        TenantProperties that = (TenantProperties) o;
        return Objects.equals(idProperty, that.idProperty) &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(tenant, that.tenant) &&
                Objects.equals(propertyType, that.propertyType) &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProperty, guid, tenant, propertyType, key, value);
    }

    @Override
    public String toString() {
        return "TenantProperties{" +
                "idProperty=" + idProperty +
                ", guid=" + guid +
                ", tenant=" + tenant +
                ", propertyType=" + propertyType +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
