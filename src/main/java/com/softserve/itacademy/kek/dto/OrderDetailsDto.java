package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.Size;
import java.util.Objects;

public class OrderDetailsDto {

    @Size(max = 4096)
    private String payload;

    @Size(max = 512)
    private String imageURL;

    public OrderDetailsDto() {
    }

    public OrderDetailsDto(String payload, String imageUrl) {
        this.payload = payload;
        this.imageURL = imageUrl;
    }

    public String getPayload() {
        return payload;
    }

    public String getImageUrl() {
        return imageURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailsDto that = (OrderDetailsDto) o;
        return Objects.equals(payload, that.payload) &&
                Objects.equals(imageURL, that.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload, imageURL);
    }

    @Override
    public String toString() {
        return "OrderDetailsDto{" +
                "payload='" + payload + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
