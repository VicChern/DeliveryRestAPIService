package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.Size;
import java.util.Objects;

import com.softserve.itacademy.kek.models.IOrderDetails;

public class OrderDetailsDto implements IOrderDetails {

    @Size(max = 4096)
    private String payload;

    @Size(max = 512)
    private String imageUrl;

    public OrderDetailsDto() {
    }

    public OrderDetailsDto(String payload, String imageUrl) {
        this.payload = payload;
        this.imageUrl = imageUrl;
    }

    public String getPayload() {
        return payload;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailsDto)) return false;
        OrderDetailsDto that = (OrderDetailsDto) o;
        return Objects.equals(payload, that.payload) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload, imageUrl);
    }

    @Override
    public String toString() {
        return "OrderDetailsDto{" +
                "payload='" + payload + '\'' +
                ", imageURL='" + imageUrl + '\'' +
                '}';
    }
}
