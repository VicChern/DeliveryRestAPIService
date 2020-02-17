package com.softserve.itacademy.kek.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.softserve.itacademy.kek.modelInterfaces.IAddress;

@Entity
@Table(name = "obj_address")
public class Address implements IAddress, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAddress;

    @NotNull
    @Column(name = "guid", unique = true, nullable = false)
    private UUID guid;

    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "address", nullable = false, length = 512)
    private String address;

    @Size(max = 1024)
    @Column(name = "notes", length = 1024)
    private String notes;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "alias", nullable = false, length = 256)
    private String alias;

    @ManyToOne
    @JoinTable(
            name = "n2o_address_tenant",
            joinColumns = @JoinColumn(name = "id_address"),
            inverseJoinColumns = @JoinColumn(name = "id_tenant")
    )
    private Tenant tenant;

    @ManyToOne
    @JoinTable(
            name = "n2o_address_user",
            joinColumns = @JoinColumn(name = "id_address"),
            inverseJoinColumns = @JoinColumn(name = "id_user")
    )
    private User user;

    public Long getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(Long idAddress) {
        this.idAddress = idAddress;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return Objects.equals(idAddress, address1.idAddress) &&
                Objects.equals(guid, address1.guid) &&
                Objects.equals(address, address1.address) &&
                Objects.equals(notes, address1.notes) &&
                Objects.equals(alias, address1.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAddress, guid, address, notes, alias);
    }

    @Override
    public String toString() {
        return "Address{" +
                "idAddress=" + idAddress +
                ", guid=" + guid +
                ", address='" + address + '\'' +
                ", notes='" + notes + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
