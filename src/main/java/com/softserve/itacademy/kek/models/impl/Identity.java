package com.softserve.itacademy.kek.models.impl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import com.softserve.itacademy.kek.models.IIdentity;

@Entity
@Table(name = "obj_identity")
public class Identity extends AbstractEntity implements Serializable, IIdentity {

    public Identity() {
    }

    public Identity(User user, @NotNull @Size(min = 1, max = 4096) String payload) {
        this.user = user;
        this.payload = payload;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIdentity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_identity_type")
    private IdentityType identityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @NotNull
    @Size(min = 1, max = 4096)
    @Column(name = "payload", nullable = false, length = 4096)
    private String payload;

    public void setIdIdentity(Long idIdentity) {
        this.idIdentity = idIdentity;
    }


    public void setPayload(String payload) {
        this.payload = payload;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    @Override
    public Long getIdIdentity() {
        return idIdentity;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public IdentityType getIdentityType() {
        return identityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equals(idIdentity, identity.idIdentity) &&
                Objects.equals(identityType, identity.identityType) &&
                Objects.equals(payload, identity.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIdentity, identityType, payload);
    }

    @Override
    public String toString() {
        return "Identity{" +
                "idIdentity=" + idIdentity +
                ", identityType=" + identityType +
                ", payload='" + payload + '\'' +
                '}';
    }
}
