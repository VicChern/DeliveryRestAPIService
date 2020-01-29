package com.softserve.itacademy.kek.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "obj_tenant")
public class Tenant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTenant;

    @OneToOne
    @JoinColumn(name ="id_tenant_owner", insertable = false, updatable = false)
    private User tenantOwner;

    @Column(name = "guid", nullable = false, unique = true)
    private UUID guid;

    @Size(min = 1, max = 256)
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "tenant")
    private TenantDetails tenantDetails;

    @OneToMany(mappedBy = "tenant")
    private List<TenantProperties> tenantPropertiesList;

    @OneToMany(mappedBy = "tenant")
    private List<Address> addressList;

    public int getIdTenant() {
        return idTenant;
    }

    public void setIdTenant(int idTenant) {
        this.idTenant = idTenant;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(String UUID) {
        this.guid = guid;
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
}
