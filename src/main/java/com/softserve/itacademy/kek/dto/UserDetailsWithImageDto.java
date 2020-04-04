package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.Size;
import java.util.Objects;

public class UserDetailsWithImageDto {
    @Size(max = 4096)
    private String payload;

    private String image;

    public UserDetailsWithImageDto() {
    }

    public UserDetailsWithImageDto(String payload, String image) {
        this.payload = payload;
        this.image = image;
    }

    public String getPayload() {
        return payload;
    }

    public String getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailsWithImageDto)) return false;
        UserDetailsWithImageDto that = (UserDetailsWithImageDto) o;
        return Objects.equals(payload, that.payload) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload, image);
    }

    @Override
    public String toString() {
        return "DetailsDto{" +
                "payload='" + payload + '\'' +
                ", imageUrl='" + image + '\'' +
                '}';
    }
}
