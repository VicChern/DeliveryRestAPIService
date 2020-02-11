package com.softserve.itacademy.kek.objects;

import javax.validation.constraints.Size;
import java.util.Objects;

public class DetailsObject {

    @Size(max = 4096)
    private String payload;

    @Size(max = 512)
    private String imageUrl;

    public String getPayload() {
        return payload;
    }

    public DetailsObject(@Size(max = 4096) String payload,
                         @Size(max = 512) String imageUrl) {
        this.payload = payload;
        this.imageUrl = imageUrl;
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
        if (o == null || getClass() != o.getClass()) return false;
        DetailsObject that = (DetailsObject) o;
        return Objects.equals(payload, that.payload) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload, imageUrl);
    }

    @Override
    public String toString() {
        return "DetailsObject{" +
                "payload='" + payload + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
