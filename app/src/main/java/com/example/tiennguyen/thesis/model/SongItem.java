package com.example.tiennguyen.thesis.model;

import java.util.ArrayList;

/**
 * Created by Quyen Hua on 11/4/2017.
 */

public class SongItem {
    private String title;
    private String views;
    private String link;
    private ArrayList<PersonItem> artist;
    private ArrayList<PersonItem> composer;
    private String linkLyric;

    public SongItem(String title, String views, String link, ArrayList<PersonItem> artist, ArrayList<PersonItem> composer, String linkLyric) {
        this.title = title;
        this.views = views;
        this.link = link;
        this.artist = artist;
        this.composer = composer;
        this.linkLyric = linkLyric;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
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
}