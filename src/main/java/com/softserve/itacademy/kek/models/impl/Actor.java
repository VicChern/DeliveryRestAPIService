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

import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;

@Entity
@Table(name = "obj_actor")
public class Actor implements IActor, Serializable {

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

    @NotNull
    @Column(name = "guid", unique = true, nullable = false)
    private UUID guid;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "alias", nullable = false, length = 256)
    private String alias;

    public Long getIdActor() {
        return idActor;
    }

    public void setIdActor(Long idActor) {
        this.idActor = idActor;
    }

    public ITenant getIdTenant() {
        return idTenant;
    }

    public void setIdTenant(Tenant idTenant) {
        this.idTenant = idTenant;
    }

    public IUser getIdUser() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(idActor, actor.idActor) &&
                Objects.equals(idTenant, actor.idTenant) &&
                Objects.equals(idUser, actor.idUser) &&
                Objects.equals(guid, actor.guid) &&
                Objects.equals(alias, actor.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idActor, idTenant, idUser, guid, alias);
    }

    @Override
    public String toString() {
        return "Actor{" +
                "idActor=" + idActor +
                ", idTenant=" + idTenant +
                ", idUser=" + idUser +
                ", guid=" + guid +
                ", alias='" + alias + '\'' +
                '}';
    }
}
