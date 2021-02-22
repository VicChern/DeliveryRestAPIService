package com.softserve.itacademy.kek.models.impl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

import com.softserve.itacademy.kek.models.IOrderDetails;

@Entity
@Table(name = "o2o_order_details")
public class OrderDetails extends AbstractEntity implements IOrderDetails, Serializable {

    @Id
    @Column(name = "id_order")
    private Long idOrder;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id_order", insertable = false, updatable = false)
    private Order order;

    @Size(max = 4096)
    @Column(name = "payload", length = 4096)
    private String payload;

    @Size(max = 512)
    @Column(name = "image_url", length = 512)
    private String imageUrl;

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetails)) return false;
        OrderDetails that = (OrderDetails) o;
        return Objects.equals(idOrder, that.idOrder) &&
                Objects.equals(payload, that.payload) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrder, payload, imageUrl);
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "idOrder=" + idOrder +
                ", payload='" + payload + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
