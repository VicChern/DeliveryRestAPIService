package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "def_order_event_type")
public class OrderEventType implements Serializable {

    @Id
    @Column(name = "id_order_event_type")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrderEventType;

    @Size(min = 1, max = 256)
    @Column(name = "name", unique = true, nullable = false)
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


}
