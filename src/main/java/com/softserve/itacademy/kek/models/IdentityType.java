package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "def_identity_type")
public class IdentityType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idIdentityType;

    @Column(name = "name")
    private String name;

    public int getIdIdentityType() {
        return idIdentityType;
    }

    public void setIdIdentityType(int idIdentityType) {
        this.idIdentityType = idIdentityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
