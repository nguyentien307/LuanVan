package com.example.tiennguyen.thesis.model;

/**
 * Created by Quyen Hua on 11/4/2017.
 */

public class AlbumItem {
    private String name;
    private String link;
    private String linkImg;
    private int views;

    public AlbumItem(String name, String link, String linkImg, int views) {
        this.name = name;
        this.link = link;
        this.linkImg = linkImg;
        this.views = views;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
