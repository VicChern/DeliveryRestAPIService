package com.softserve.itacademy.kek.models;


import com.softserve.itacademy.kek.modelInterfaces.IUserDetailsData;

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
@Table(name = "o2o_user_details")
public class UserDetails implements IUserDetailsData, Serializable {

    @Id
    @Column(name = "id_user")
    private Long idUser;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private User user;

    @Size(max = 4096)
    @Column(name = "payload", length = 4096)
    private String payload;

    @Size(max = 512)
    @Column(name = "image_url", length = 512)
    private String imageUrl;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetails that = (UserDetails) o;
        return Objects.equals(idUser, that.idUser) &&
                Objects.equals(user, that.user) &&
                Objects.equals(payload, that.payload) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, user, payload, imageUrl);
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "id=" + idUser +
                ", payload='" + payload + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
