package com.merit.myapplication.models;

/**
 * Created by merit on 7/12/2015.
 */
public class Comment {
    String id, createdTime, text;
    User user;

    public Comment(String id, String createdTime, String text, User user) {
        this.id = id;
        this.createdTime = createdTime;
        this.text = text;
        this.user = user;
    }

    public Comment() {
        this.id = null;
        this.createdTime = null;
        this.text = null;
        this.user = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
