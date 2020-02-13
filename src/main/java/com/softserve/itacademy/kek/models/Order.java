package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.modelInterfaces.IOrder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "obj_order")
public class Order implements IOrder, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long idOrder;

    @ManyToOne
    @JoinColumn(name = "id_tenant", insertable = false, updatable = false)
    private Tenant idTenant;

    @NotNull
    @Column(name = "guid", unique = true, nullable = false)
    private UUID guid;

    @NotNull
    @Size(min = 1, max = 256, message = "summary must be in range 1 ... 256")
    @Column(name = "summary", nullable = false, length = 256)
    private String summary;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private OrderDetails orderDetails;

    @OneToMany(mappedBy = "idOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderEvent> orderEventList;

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Tenant getIdTenant() {
        return idTenant;
    }

    public void setIdTenant(Tenant idTenant) {
        this.idTenant = idTenant;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<OrderEvent> getOrderEventList() {
        return orderEventList;
    }

    public void setOrderEventList(List<OrderEvent> orderEventList) {
        this.orderEventList = orderEventList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(idOrder, order.idOrder) &&
                Objects.equals(idTenant, order.idTenant) &&
                Objects.equals(guid, order.guid) &&
                Objects.equals(summary, order.summary) &&
                Objects.equals(orderDetails, order.orderDetails) &&
                Objects.equals(orderEventList, order.orderEventList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrder, idTenant, guid, summary, orderDetails, orderEventList);
    }

    @Override
    public String toString() {
        return "Order{" +
                "idOrder=" + idOrder +
                ", tenant=" + idTenant +
                ", guid=" + guid +
                ", summary='" + summary + '\'' +
                ", orderDetails=" + orderDetails +
                ", orderEventList=" + orderEventList +
                '}';
    }
}
