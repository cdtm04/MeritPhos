package com.merit.myapplication.models;

import java.io.Serializable;

/**
 * Created by merit on 7/12/2015.
 */
public class User implements Serializable {
    String userName, profilePicture, id, fullName;
    String outgoing_status, incoming_status;

    public User(String userName, String profilePicture, String id, String fullName, String outgoing_status, String incoming_status) {
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.id = id;
        this.fullName = fullName;
        this.incoming_status = incoming_status;
        this.outgoing_status = outgoing_status;
    }

    public User() {
        this.userName = null;
        this.profilePicture = null;
        this.id = null;
        this.fullName = null;
        this.outgoing_status = null;
        this.incoming_status = null;
    }

    public String getOutgoing_status() {
        return outgoing_status;
    }

    public void setOutgoing_status(String outgoing_status) {
        this.outgoing_status = outgoing_status;
    }

    public String getIncoming_status() {
        return incoming_status;
    }

    public void setIncoming_status(String incoming_status) {
        this.incoming_status = incoming_status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
