package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.modelInterfaces.IUser;
import com.softserve.itacademy.kek.modelInterfaces.IUserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "obj_user")
public class User implements IUser, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @NotNull
    @Column(name = "guid", nullable = false, unique = true)
    private UUID guid;

    @Email
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "email", nullable = false, unique = true, length = 256)
    private String email;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "phone_number", nullable = false, unique = true, length = 256)
    private String phoneNumber;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "nickname", nullable = false, unique = true, length = 256)
    private String nickname;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserDetails userDetails;

    @OneToOne(mappedBy = "tenantOwner", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Tenant tenant;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Identity> identityList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Address> addressList;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long id) {
        this.idUser = id;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public IUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
        userDetails.setUser(this);
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public List<Identity> getIdentityList() {
        return identityList;
    }

    public void setIdentityList(List<Identity> identityList) {
        this.identityList = identityList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    //Bidirectional @OneToMany
    //https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#associations-one-to-many
    public void addIdentity(Identity identity) {
        identityList.add(identity);
        identity.setUser(this);
    }

    public void removeIdentity(Identity identity) {
        identityList.remove(identity);
        identity.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(idUser, user.idUser) &&
                Objects.equals(guid, user.guid) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(name, user.name) &&
                Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, guid, email, phoneNumber, name, nickname);
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", guid='" + guid + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userDetails=" + userDetails +
                '}';
    }
}
