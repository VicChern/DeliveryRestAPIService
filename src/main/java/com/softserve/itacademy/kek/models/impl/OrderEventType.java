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

import com.softserve.itacademy.kek.models.IOrderEventType;

@Entity
@Table(name = "def_order_event_type")
public class OrderEventType extends AbstractEntity implements IOrderEventType, Serializable {

    @Id
    @Column(name = "id_order_event_type")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrderEventType;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name", unique = true, nullable = false, length = 256)
    private String name;

    public Long getIdOrderEventType() {
        return idOrderEventType;
    }

    public void setIdOrderEventType(Long idOrderEventType) {
        this.idOrderEventType = idOrderEventType;
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
        if (o == null || getClass() != o.getClass()) return false;
        OrderEventType that = (OrderEventType) o;
        return Objects.equals(idOrderEventType, that.idOrderEventType) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrderEventType, name);
    }

    @Override
    public String toString() {
        return "OrderEventType{" +
                "idOrderEventType=" + idOrderEventType +
                ", name='" + name + '\'' +
                '}';
    }
}
