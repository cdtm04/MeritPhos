package com.merit.myapplication.models;

import android.graphics.Bitmap;

/**
 * Created by merit on 7/10/2015.
 */
public class UserProfile {
    private int countPosts, countFollowers, countFollowing;
    private String userName, fullName, link, bio, id;
    private String avatar;
    private String incoming, outgoing;

    public UserProfile(int countPosts, int countFollowers, int countFollowing, String userName, String fullName, String link, String bio, String id, String avatar, String incoming, String outgoing) {
        this.countPosts = countPosts;
        this.countFollowers = countFollowers;
        this.countFollowing = countFollowing;
        this.userName = userName;
        this.fullName = fullName;
        this.link = link;
        this.bio = bio;
        this.id = id;
        this.avatar = avatar;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    public String getIncoming() {
        return incoming;
    }

    public void setIncoming(String incoming) {
        this.incoming = incoming;
    }

    public String getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(String outgoing) {
        this.outgoing = outgoing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValues(String id, String userName, int countPosts, int countFollowers, int countFollowing, String fullName, String link, String bio, String avatar, String incoming, String outgoing) {
        this.id = id;
        this.userName = userName;
        this.countPosts = countPosts;
        this.countFollowers = countFollowers;
        this.countFollowing = countFollowing;
        this.fullName = fullName;
        this.link = link;
        this.bio = bio;
        this.avatar = avatar;
        this.incoming = incoming;
        this.outgoing = outgoing;
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
