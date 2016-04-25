package com.tshirtapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tshirtapp.domain.util.Const;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Amine on 12/22/15.
 */

@Document(collection = "t_user")
public class User implements Serializable {

    @Id
    private String id;
    private String fbID;
    private String username;
    private String firstName;
    private String lastName;
    private String wilaya;
    private String phoneNumber;
    private String urlFb;
    private String status;
    private Set<Authority> authorities = new HashSet<>();
    private String pictureUri;
    private Long clientRef;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Field("created_date")
    private ZonedDateTime createdDate;

    @Field("last_modified_date  ")
    private ZonedDateTime lastModifiedDate;

    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getUrlFb() {
        return urlFb;
    }

    public void setUrlFb(String urlFb) {
        this.urlFb = urlFb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
            this.status=status;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {

        if(this.getId()==null){
            this.createdDate  = ZonedDateTime.now();
        }
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        if(this.getId()!=null){
            this.lastModifiedDate = ZonedDateTime.now();
        }

    }

    public Long getClientRef() {
        return clientRef;
    }

    public void setClientRef(Long clientRef) {
        this.clientRef = clientRef;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
