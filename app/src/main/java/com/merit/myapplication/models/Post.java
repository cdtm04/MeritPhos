package com.merit.myapplication.models;

import java.util.ArrayList;

/**
 * Created by merit on 7/12/2015.
 */
public class Post {
    Location locationOfPost;
    String filter, createdTime, link, type, id;
    Caption captionOfPost;
    ArrayList<Comment> comments;
    ArrayList<User> likes;
    Media media;
    User user;

    public Post(Location location, String filter, String createdTime, String link, Caption captionOfPost, String type, String id, ArrayList<Comment> comments, ArrayList<User> likes, Media media, User user) {
        this.locationOfPost = location;
        this.filter = filter;
        this.createdTime = createdTime;
        this.link = link;
        this.captionOfPost = captionOfPost;
        this.type = type;
        this.id = id;
        this.comments = comments;
        this.likes = likes;
        this.media = media;
        this.user = user;
    }

    public Post() {
        this.locationOfPost = null;
        this.filter = "";
        this.createdTime = "";
        this.link = "";
        this.captionOfPost = null;
        this.type = "";
        this.id = "";
        this.comments = null;
        this.likes = null;
        this.media = null;
        this.user = null;
    }

    public Location getLocationOfPost() {
        return locationOfPost;
    }

    public void setLocationOfPost(Location location) {
        this.locationOfPost = location;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Caption getCaptionOfPost() {
        return captionOfPost;
    }

    public void setCaptionOfPost(Caption captionOfPost) {
        this.captionOfPost = captionOfPost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<User> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<User> likes) {
        this.likes = likes;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLiked(String userName) {
        for (int i = 0; i < likes.size(); i++) {
            if (userName.equals(likes.get(i).getUserName())) return true;
        }
        return false;
    }
}
