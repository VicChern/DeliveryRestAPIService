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
import java.util.UUID;

@Entity
@Table(name = "obj_actor")
public class Actor implements Serializable {

    @Id
    @Column(name = "id_actor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActor;

    @ManyToOne
    @JoinColumn(name = "id_tenant")
    private Tenant idTenant;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User idUser;

    @Column(name = "guid", unique = true, nullable = false)
    private UUID guid;

    @Size(min = 1, max = 256)
    @Column(name = "alias", nullable = false)
    private String alias;

    public Long getIdActor() {
        return idActor;
    }

    public void setIdActor(Long idActor) {
        this.idActor = idActor;
    }

    public Tenant getIdTenant() {
        return idTenant;
    }

    public void setIdTenant(Tenant idTenant) {
        this.idTenant = idTenant;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
