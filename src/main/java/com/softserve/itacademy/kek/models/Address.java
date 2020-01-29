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
import java.util.UUID;

@Entity
@Table(name = "obj_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAddress;

    @Column(name = "guid", unique = true, nullable = false)
    private UUID guid;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "notes")
    private String notes;

    @Column(name = "alias", nullable = false)
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


}
