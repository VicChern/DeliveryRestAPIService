package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "obj_tenant_properties")
public class TenantProperties implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProperty;

    @ManyToOne
    @JoinColumn(name = "id_tenant", insertable = false, updatable = false)
    Tenant tenant;

    @OneToOne
    @JoinColumn(name = "id_property_type", insertable = false, updatable = false)
    private PropertyType propertyType;

    @Size(min = 1, max = 256)
    @Column(name = "key", nullable = false, unique = true, length = 256)
    private String key;

    @Size(min = 1, max = 4096)
    @Column(name = "value", nullable = false, length = 4096)
    private String value;

    public Long getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(Long idProperty) {
        this.idProperty = idProperty;
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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantProperties that = (TenantProperties) o;
        return Objects.equals(idProperty, that.idProperty) &&
                Objects.equals(tenant, that.tenant) &&
                Objects.equals(propertyType, that.propertyType) &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProperty, tenant, propertyType, key, value);
    }

    @Override
    public String toString() {
        return "TenantProperties{" +
                "idProperty=" + idProperty +
                ", propertyType=" + propertyType +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
