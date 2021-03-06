package com.example.tiennguyen.thesis.model;

import java.util.ArrayList;

/**
 * Created by Quyen Hua on 11/4/2017.
 */

public class SongItem {
    private String title;
    private int views;
    private String link;
    private ArrayList<PersonItem> artist;
    private ArrayList<PersonItem> composer;
    private String linkLyric;


    private String linkImg;

    public SongItem(String title, int views, String link, ArrayList<PersonItem> artist, ArrayList<PersonItem> composer, String linkLyric, String linkImg) {
        this.title = title;
        this.views = views;
        this.link = link;
        this.artist = artist;
        this.composer = composer;
        this.linkLyric = linkLyric;
        this.linkImg = linkImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<PersonItem> getArtist() {
        return artist;
    }

    public void setArtist(ArrayList<PersonItem> artist) {
        this.artist = artist;
    }

    public ArrayList<PersonItem> getComposer() {
        return composer;
    }

    public void setComposer(ArrayList<PersonItem> composer) {
        this.composer = composer;
    }

    public String getLinkLyric() {
        return linkLyric;
    }

    public void setLinkLyric(String linkLyric) {
        this.linkLyric = linkLyric;
    }
    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

}
