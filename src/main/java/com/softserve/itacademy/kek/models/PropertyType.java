package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "def_property_type")
public class PropertyType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPropertyType;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "schema", nullable = false)
    private String schema;

    public int getIdPropertyType() {
        return idPropertyType;
    }

    public void setIdPropertyType(int idPropertyType) {
        this.idPropertyType = idPropertyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }


}
