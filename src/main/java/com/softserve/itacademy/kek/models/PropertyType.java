package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "def_property_type")
public class PropertyType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPropertyType;

    @Size(min = 1, max = 256)
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Size(min = 1)
    @Column(name = "schema", nullable = false)
    private String schema;

    public Long getIdPropertyType() {
        return idPropertyType;
    }

    public void setIdPropertyType(Long idPropertyType) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyType that = (PropertyType) o;
        return Objects.equals(idPropertyType, that.idPropertyType) &&
                Objects.equals(name, that.name) &&
                Objects.equals(schema, that.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPropertyType, name, schema);
    }

    @Override
    public String toString() {
        return "PropertyType{" +
                "idPropertyType=" + idPropertyType +
                ", name='" + name + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }
}
