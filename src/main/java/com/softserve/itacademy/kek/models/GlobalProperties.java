package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "obj_global_properties")
public class GlobalProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProperty;

    @OneToOne
    @JoinColumn(name = "id_property_type", insertable = false, updatable = false)
    private PropertyType propertyType;

    @Column(name = "key", unique = true, nullable = false)
    private int key;

    @Column(name = "value", nullable = false)
    private String value;

    public int getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(int idProperty) {
        this.idProperty = idProperty;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
