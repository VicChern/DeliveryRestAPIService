package com.softserve.itacademy.kek.models.impl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;

@Entity
@Table(name = "obj_actor")
public class Actor extends AbstractEntity implements IActor, Serializable {

    @Id
    @Column(name = "id_actor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActor;

    // TODO: 09.04.2020 ??? cascade deleting
    @ManyToOne
    @JoinColumn(name = "id_tenant")
    private Tenant tenant;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @NotNull
    @Column(name = "guid", unique = true, nullable = false)
    private UUID guid;

    @ManyToMany
    private List<ActorRole> actorRoles = new ArrayList<>();

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "alias", nullable = false, length = 256)
    private String alias;

    public List<ActorRole> getActorRoles() {
        return actorRoles;
    }

    public void setActorRoles(List<ActorRole> actorRoles) {
        this.actorRoles = actorRoles;
    }

    public Long getIdActor() {
        return idActor;
    }

    public void setIdActor(Long idActor) {
        this.idActor = idActor;
    }

    public ITenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public IUser getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void addActorRole(ActorRole actorRole) {
        actorRoles.add(actorRole);
    }

    public void removeTenantProperty(ActorRole actorRole) {
        actorRoles.remove(actorRole);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor)) return false;
        Actor actor = (Actor) o;
        return Objects.equals(idActor, actor.idActor) &&
                Objects.equals(tenant, actor.tenant) &&
                Objects.equals(user, actor.user) &&
                Objects.equals(guid, actor.guid) &&
                Objects.equals(alias, actor.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idActor, tenant, user, guid, alias);
    }

    @Override
    public String toString() {
        return "Actor{" +
                "idActor=" + idActor +
                ", idTenant=" + tenant +
                ", idUser=" + user +
                ", guid=" + guid +
                ", alias='" + alias + '\'' +
                '}';
    }
}
