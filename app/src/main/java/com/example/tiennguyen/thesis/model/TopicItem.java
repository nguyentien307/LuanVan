package com.example.tiennguyen.thesis.model;

/**
 * Created by TIENNGUYEN on 11/6/2017.
 */

public class TopicItem {
    private String title;
    private String detail;
    private int image;
    //private boolean isExpandable;

    //    public AlbumTopicItem(String topicTitle, ArrayList<AlbumItem> arrAlbum, boolean isExpandable) {
//        this.topicTitle = topicTitle;
//        this.arrAlbum = arrAlbum;
//        //this.isExpandable = isExpandable;
//    }
    public TopicItem(String title, String detail, int image) {
        this.title = title;
        this.detail = detail;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setDetail(String detail) {
        this.detail = detail;

    }
}
