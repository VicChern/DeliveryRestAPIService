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

import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;

@Entity
@Table(name = "def_identity_type")
public class IdentityType extends AbstractEntity implements Serializable {
    public IdentityType() {
    }

    public IdentityType(IdentityTypeEnum identityTypeEnum) {
        this.name = identityTypeEnum.name();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_identity_type")
    private Long idIdentityType;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name", nullable = false, unique = true, length = 256)
    private String name;

    public Long getIdIdentityType() {
        return idIdentityType;
    }

    public void setIdIdentityType(Long idIdentityType) {
        this.idIdentityType = idIdentityType;
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
        if (!(o instanceof IdentityType)) return false;
        IdentityType that = (IdentityType) o;
        return Objects.equals(idIdentityType, that.idIdentityType) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIdentityType, name);
    }

    @Override
    public String toString() {
        return "IdentityType{" +
                "idIdentityType=" + idIdentityType +
                ", name='" + name + '\'' +
                '}';
    }
}
