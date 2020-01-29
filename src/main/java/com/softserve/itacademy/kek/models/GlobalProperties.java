package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "obj_global_properties")
public class GlobalProperties implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProperty;

    @OneToOne
    @JoinColumn(name = "id_property_type", insertable = false, updatable = false)
    private PropertyType propertyType;

    @Size(min = 1, max = 256)
    @Column(name = "key", unique = true, nullable = false)
    private String key;

    @Size(min = 1, max = 4096)
    @Column(name = "value", nullable = false)
    private String value;

    public Long getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(Long idProperty) {
        this.idProperty = idProperty;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
