package com.softserve.itacademy.kek.models;

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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "obj_identity")
public class Identity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIdentity;

    @OneToOne
    @JoinColumn(name = "id_identity_type", insertable = false, updatable = false)
    private IdentityType identityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private User user;

    @Size(min = 1, max = 4096)
    @Column(name = "payload", nullable = false)
    private String payload;

    public Long getIdIdentity() {
        return idIdentity;
    }

    public void setIdIdentity(Long idIdentity) {
        this.idIdentity = idIdentity;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equals(idIdentity, identity.idIdentity) &&
                Objects.equals(identityType, identity.identityType) &&
                Objects.equals(user, identity.user) &&
                Objects.equals(payload, identity.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIdentity, identityType, user, payload);
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
