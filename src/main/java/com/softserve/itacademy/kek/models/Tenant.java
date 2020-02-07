package com.softserve.itacademy.kek.models;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "obj_tenant")
public class Tenant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tenant")
    private Long idTenant;

    @OneToOne
    @JoinColumn(name ="id_tenant_owner")
    private User tenantOwner;

    @NotNull
    @Column(name = "guid", nullable = false, unique = true)
    private UUID guid;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name", nullable = false, unique = true, length = 256)
    private String name;

    @OneToOne(mappedBy = "tenant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private TenantDetails tenantDetails;

    @OneToMany(mappedBy = "tenant")
    private List<TenantProperties> tenantPropertiesList;

    @OneToMany(mappedBy = "tenant")
    private List<Address> addressList;

    public Long getIdTenant() {
        return idTenant;
    }

    public void setIdTenant(Long idTenant) {
        this.idTenant = idTenant;
    }

    public UUID getGuid() {
        return guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getTenantOwner() {
        return tenantOwner;
    }

    public void setTenantOwner(User tenantOwner) {
        this.tenantOwner = tenantOwner;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public TenantDetails getTenantDetails() {
        return tenantDetails;
    }

    public void setTenantDetails(TenantDetails tenantDetails) {
        this.tenantDetails = tenantDetails;
    }

    public List<TenantProperties> getTenantPropertiesList() {
        return tenantPropertiesList;
    }

    public void setTenantPropertiesList(List<TenantProperties> tenantPropertiesList) {
        this.tenantPropertiesList = tenantPropertiesList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tenant tenant = (Tenant) o;
        return Objects.equals(idTenant, tenant.idTenant) &&
                Objects.equals(tenantOwner, tenant.tenantOwner) &&
                Objects.equals(guid, tenant.guid) &&
                Objects.equals(name, tenant.name) &&
                Objects.equals(tenantDetails, tenant.tenantDetails) &&
                Objects.equals(tenantPropertiesList, tenant.tenantPropertiesList) &&
                Objects.equals(addressList, tenant.addressList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTenant, tenantOwner, guid, name, tenantDetails, tenantPropertiesList, addressList);
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "idTenant=" + idTenant +
                ", tenantOwner=" + tenantOwner +
                ", guid=" + guid +
                ", name='" + name + '\'' +
                ", tenantDetails=" + tenantDetails +
                '}';
    }
}
