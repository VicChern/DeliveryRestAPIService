package com.softserve.itacademy.kek.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "o2o_user_details")
public class UserDetails implements Serializable {

    @Id
    @Column(name = "id_user")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "id_user")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
