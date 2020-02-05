package com.softserve.itacademy.kek.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "o2o_user_details")
public class UserDetails implements Serializable {

    @Id
    @Column(name = "id_user")
//    @GeneratedValue(generator="gen")
//    @GenericGenerator(name="gen", strategy="foreign",
//            parameters = @Parameter(name="property", value="user"))
    private Long idUser;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
//    @PrimaryKeyJoinColumn
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

    // derived identifiers
    //https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#identifiers-derived-primarykeyjoincolumn
    public void setUser(User user) {
        this.user = user;
        this.idUser = user.getIdUser();
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
