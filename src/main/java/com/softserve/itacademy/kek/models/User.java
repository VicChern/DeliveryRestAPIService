package com.softserve.itacademy.kek.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "obj_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "guid", nullable = false, unique = true)
    private String guid;

    @Email
    @Size(min = 1, max = 256)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|\n" +
            "2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|\n" +
            "4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$")
    @Size(min = 1, max = 256)
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Size(min = 1, max = 256)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 1, max = 256)
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @OneToOne(mappedBy = "user")
    private UserDetails userDetails;

    @OneToOne(mappedBy = "tenantOwner")
    private Tenant tenant;

    @OneToMany(mappedBy = "user")
    List<Identity> identityList;

    @OneToMany(mappedBy = "user")
    List<Address> addressList;

    public Long getIdUser() {
        return idUser;
    }

    public String getGuid() {
        return guid;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setIdUser(Long id) {
        this.idUser = id;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
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
}
