package com.merit.myapplication.models;

/**
 * Created by merit on 7/13/2015.
 */
public class Caption {
    String idOfCaption, createdTimeOfCaption, textOfCaption;
    User userOfCaption;

    public Caption(String idOfCaption, String createdTimeOfCaption, String textOfCaption, User userOfCaption) {
        this.idOfCaption = idOfCaption;
        this.createdTimeOfCaption = createdTimeOfCaption;
        this.textOfCaption = textOfCaption;
        this.userOfCaption = userOfCaption;
    }

    public Caption() {
        this.idOfCaption = "";
        this.createdTimeOfCaption = "";
        this.textOfCaption = "";
        this.userOfCaption = null;
    }

    public String getIdOfCaption() {
        return idOfCaption;
    }

    public void setIdOfCaption(String idOfCaption) {
        this.idOfCaption = idOfCaption;
    }

    public String getCreatedTimeOfCaption() {
        return createdTimeOfCaption;
    }

    public void setCreatedTimeOfCaption(String createdTimeOfCaption) {
        this.createdTimeOfCaption = createdTimeOfCaption;
    }

    public String getTextOfCaption() {
        return textOfCaption;
    }

    public void setTextOfCaption(String textOfCaption) {
        this.textOfCaption = textOfCaption;
    }

    public User getUserOfCaption() {
        return userOfCaption;
    }

    public void setUserOfCaption(User userOfCaption) {
        this.userOfCaption = userOfCaption;
    }
}
