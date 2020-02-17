package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.modelInterfaces.IOrderEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import java.util.UUID;

@Entity
@Table(name = "obj_order_event")
public class OrderEvent implements IOrderEvent, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_event")
    private Long idOrderEvent;

    @ManyToOne
    @JoinColumn(name = "id_order", insertable = false, updatable = false)
    private Order idOrder;

    @ManyToOne
    @JoinColumn(name = "id_actor")
    private Actor idActor;

    @OneToOne
    @JoinColumn(name = "id_order_event_type")
    private OrderEventType idOrderEventType;

    @NotNull
    @Column(name = "guid", unique = true, nullable = false)
    private UUID guid;

    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "payload", nullable = false, length = 1024)
    private String payload;

    public Long getIdOrderEvent() {
        return idOrderEvent;
    }

    public void setIdOrderEvent(Long idOrderEvent) {
        this.idOrderEvent = idOrderEvent;
    }

    public Order getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Order idOrder) {
        this.idOrder = idOrder;
    }

    public Actor getIdActor() {
        return idActor;
    }

    public void setIdActor(Actor idActor) {
        this.idActor = idActor;
    }

    public OrderEventType getIdOrderEventType() {
        return idOrderEventType;
    }

    public void setIdOrderEventType(OrderEventType idOrderEventType) {
        this.idOrderEventType = idOrderEventType;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEvent that = (OrderEvent) o;
        return Objects.equals(idOrderEvent, that.idOrderEvent) &&
                Objects.equals(idOrder, that.idOrder) &&
                Objects.equals(idActor, that.idActor) &&
                Objects.equals(idOrderEventType, that.idOrderEventType) &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrderEvent, idOrder, idActor, idOrderEventType, guid, payload);
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "idOrderEvent=" + idOrderEvent +
                ", idOrderEventType=" + idOrderEventType +
                ", guid=" + guid +
                ", payload='" + payload + '\'' +
                '}';
    }
}
