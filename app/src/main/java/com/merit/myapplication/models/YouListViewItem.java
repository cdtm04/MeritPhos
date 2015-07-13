package com.merit.myapplication.models;

import android.graphics.drawable.Drawable;

/**
 * Created by merit on 6/29/2015.
 */
public class YouListViewItem {
    private Drawable avatar;
    private Drawable button;
    private String content;

    public YouListViewItem(Drawable avatar, Drawable button, String content) {
        this.avatar = avatar;
        this.button = button;
        this.content = content;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
    }

    public Drawable getButton() {
        return button;
    }

    public void setButton(Drawable button) {
        this.button = button;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
