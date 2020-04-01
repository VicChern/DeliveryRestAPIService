package com.softserve.itacademy.kek.models.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "def_actor_role")
public class ActorRole extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "id_actor_role")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActorRole;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name", unique = true, nullable = false, length = 256)
    private String name;

    public Long getIdActorRole() {
        return idActorRole;
    }

    public void setIdActorRole(Long idActorRole) {
        this.idActorRole = idActorRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActorRole)) return false;
        ActorRole actorRole = (ActorRole) o;
        return Objects.equals(idActorRole, actorRole.idActorRole) &&
                Objects.equals(name, actorRole.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idActorRole, name);
    }

    @Override
    public String toString() {
        return "ActorRole{" +
                "idActorRole=" + idActorRole +
                ", name='" + name + '\'' +
                '}';
    }
}
