package com.softserve.itacademy.kek.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "o2o_tenant_details")
public class TenantDetails implements Serializable {

    @Id
    @Column(name = "id_tenant")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
//    @PrimaryKeyJoinColumn(name = "id_tenant")
    private Tenant tenant;

    @Size(max = 4096)
    @Column(name = "payload", length = 4096)
    private String payload;

    @Size(max = 512)
    @Column(name = "image_url", length = 512)
    private String imageUrl;

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
        return Objects.equals(id, that.id) &&
                Objects.equals(tenant, that.tenant) &&
                Objects.equals(payload, that.payload) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenant, payload, imageUrl);
    }

    @Override
    public String toString() {
        return "TenantDetails{" +
                "id=" + id +
                ", tenant=" + tenant +
                ", payload='" + payload + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
