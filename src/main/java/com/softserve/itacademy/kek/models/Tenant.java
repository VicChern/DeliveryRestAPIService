package com.softserve.itacademy.kek.models;


import com.softserve.itacademy.kek.dataexchange.ITenant;
import com.softserve.itacademy.kek.dataexchange.ITenantDetails;
import com.softserve.itacademy.kek.dataexchange.IUser;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "obj_tenant")
public class Tenant implements ITenant, Serializable {

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

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TenantProperties> tenantPropertiesList  = new ArrayList<>();

    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY)
    private List<Address> addressList;

    @OneToMany(mappedBy = "idTenant", fetch = FetchType.LAZY)
    private List<Order> orderList;

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

    public IUser getTenantOwner() {
        return tenantOwner;
    }

    public void setTenantOwner(IUser tenantOwner) {
        this.tenantOwner = (User)tenantOwner;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public ITenantDetails getTenantDetails() {
        return tenantDetails;
    }

    public void setTenantDetails(ITenantDetails tenantDetails) {
        this.tenantDetails = (TenantDetails) tenantDetails;
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

    // see bidirectional @OneToMany association https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#associations-one-to-many
    public void addTenantProperty(TenantProperties tenantProperty) {
        tenantPropertiesList.add( tenantProperty );
        tenantProperty.setTenant( this );
    }

    public void removeTenantProperty(TenantProperties tenantProperty) {
        tenantPropertiesList.remove( tenantProperty );
        tenantProperty.setTenant( null );
    }

    public void setTenantOwner(User tenantOwner) {
        this.tenantOwner = tenantOwner;
    }

    public void setTenantDetails(TenantDetails tenantDetails) {
        this.tenantDetails = tenantDetails;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tenant tenant = (Tenant) o;
        return Objects.equals(idTenant, tenant.idTenant) &&
                Objects.equals(tenantOwner, tenant.tenantOwner) &&
                Objects.equals(guid, tenant.guid) &&
                Objects.equals(name, tenant.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTenant, tenantOwner, guid, name);
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
