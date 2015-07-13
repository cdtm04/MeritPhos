package com.merit.myapplication.models;

import android.graphics.Bitmap;

/**
 * Created by merit on 7/10/2015.
 */
public class UserProfile {
    private int countPosts, countFollowers, countFollowing;
    private String userName, fullName, link, bio, id;
    private String avatar;

    public UserProfile(String id, String userName, int countPosts, int countFollowers, int countFollowing, String fullName, String link, String bio, String avatar) {
        this.id = id;
        this.userName = userName;
        this.countPosts = countPosts;
        this.countFollowers = countFollowers;
        this.countFollowing = countFollowing;
        this.fullName = fullName;
        this.link = link;
        this.bio = bio;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValues(String id, String userName, int countPosts, int countFollowers, int countFollowing, String fullName, String link, String bio, String avatar) {
        this.id = id;
        this.userName = userName;
        this.countPosts = countPosts;
        this.countFollowers = countFollowers;
        this.countFollowing = countFollowing;
        this.fullName = fullName;
        this.link = link;
        this.bio = bio;
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCountPosts() {
        return countPosts;
    }

    public void setCountPosts(int countPosts) {
        this.countPosts = countPosts;
    }

    public int getCountFollowers() {
        return countFollowers;
    }

    public void setCountFollowers(int countFollowers) {
        this.countFollowers = countFollowers;
    }

    public int getCountFollowing() {
        return countFollowing;
    }

    public void setCountFollowing(int countFollowing) {
        this.countFollowing = countFollowing;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
