package com.softserve.itacademy.kek.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "o2o_tenant_details")
public class TenantDetails implements Serializable {

    @Id
    @Column(name = "id_tenant")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "id_tenant")
    private Tenant tenant;

    @Size(max = 4096)
    @Column(name = "payload")
    private String payload;

    @Size(max = 512)
    @Column(name = "image_url")
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
}
