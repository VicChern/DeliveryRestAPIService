package com.softserve.itacademy.kek.dto;

import com.softserve.itacademy.kek.models.ITenantDetails;

import javax.validation.constraints.Size;
import java.util.Objects;

public class TenantDetailsDto implements ITenantDetails {

    @Size(max = 4096)
    private String payload;

    @Size(max = 512)
    private String imageUrl;

    public TenantDetailsDto() {
    }

    public TenantDetailsDto(String payload, String imageUrl) {
        this.payload = payload;
        this.imageUrl = imageUrl;
    }

    public String getPayload() {
        return payload;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "TenantDetailsDto{" +
                "payload='" + payload + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantDetailsDto)) return false;
        TenantDetailsDto that = (TenantDetailsDto) o;
        return payload.equals(that.payload) &&
                imageUrl.equals(that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload, imageUrl);
    }
}
