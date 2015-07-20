package com.merit.myapplication.models;

import java.io.Serializable;

/**
 * Created by merit on 7/12/2015.
 */
public class Image implements Serializable {
    String url;
    int height, width;

    public Image(String url, int height, int width) {
        this.url = url;
        this.height = height;
        this.width = width;
    }

    public Image() {
        this.url = null;
        this.height = 0;
        this.width = 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
