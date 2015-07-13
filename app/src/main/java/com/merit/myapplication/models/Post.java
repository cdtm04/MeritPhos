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
    int countLikes, countComments;
    Image image;
    User userOfPost;
    String video;
    boolean isLiked;

    public Post(Location location, String filter, String createdTime, String link, Caption captionOfPost, String type, String id, int countComments, ArrayList<Comment> comments, int countLikes, ArrayList<User> likes, Image image, String video, User userOfPost, boolean isLiked) {
        this.locationOfPost = location;
        this.filter = filter;
        this.createdTime = createdTime;
        this.link = link;
        this.captionOfPost = captionOfPost;
        this.type = type;
        this.id = id;
        this.comments = comments;
        this.likes = likes;
        this.image = image;
        this.userOfPost = userOfPost;
        this.countLikes = countLikes;
        this.countComments = countComments;
        this.video = video;
        this.isLiked = isLiked;
    }

    public Post() {
        this.locationOfPost = null;
        this.filter = null;
        this.createdTime = null;
        this.link = null;
        this.captionOfPost = null;
        this.type = null;
        this.id = null;
        this.comments = null;
        this.likes = null;
        this.image = null;
        this.userOfPost = null;
        this.countLikes = 0;
        this.countComments = 0;
        this.video = null;
        this.isLiked = false;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(int countLikes) {
        this.countLikes = countLikes;
    }

    public int getCountComments() {
        return countComments;
    }

    public void setCountComments(int countComments) {
        this.countComments = countComments;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public User getUserOfPost() {
        return userOfPost;
    }

    public void setUserOfPost(User userOfPost) {
        this.userOfPost = userOfPost;
    }

}
