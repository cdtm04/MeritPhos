package com.merit.myapplication.models;

/**
 * Created by merit on 7/12/2015.
 */
public class User {
    String userName, profilePicture, id, fullName;

    public User(String userName, String profilePicture, String id, String fullName) {
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.id = id;
        this.fullName = fullName;
    }

    public User() {
        this.userName = "";
        this.profilePicture = "";
        this.id = "";
        this.fullName = "";
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
