package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "o2o_tenant_details")
public class TenantDetails implements Serializable {

    @Id
    @Column(name = "id_tenant")
    private Long idTenant;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_tenant", insertable = false, updatable = false)
    private Tenant tenant;

    @Size(max = 4096)
    @Column(name = "payload", length = 4096)
    private String payload;

    @Size(max = 512)
    @Column(name = "image_url", length = 512)
    private String imageUrl;

    public Long getIdTenant() {
        return idTenant;
    }

    public void setIdTenant(Long idTenant) {
        this.idTenant = idTenant;
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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantDetails that = (TenantDetails) o;
        return Objects.equals(idTenant, that.idTenant) &&
                Objects.equals(payload, that.payload) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTenant, payload, imageUrl);
    }

    @Override
    public String toString() {
        return "TenantDetails{" +
                "id=" + idTenant +
                ", payload='" + payload + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
