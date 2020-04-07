package com.asis.finalproject;

public class BbcFavItem {
    private int id;
    private String fav_title;
    private String fav_pubDate;
    private String fav_description;
    private String fav_webUrl;

    public BbcFavItem(){
    }

    public BbcFavItem(int id, String fav_title, String fav_pubDate, String fav_description, String fav_webUrl) {
        this.id = id;
        this.fav_title = fav_title;
        this.fav_pubDate = fav_pubDate;
        this.fav_description = fav_description;
        this.fav_webUrl = fav_webUrl;
    }

    public BbcFavItem(String fav_title, String fav_pubDate, String fav_description, String fav_webUrl) {
        this.fav_title = fav_title;
        this.fav_pubDate = fav_pubDate;
        this.fav_description = fav_description;
        this.fav_webUrl = fav_webUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFav_title() {
        return fav_title;
    }

    public void setFav_title(String fav_title) {
        this.fav_title = fav_title;
    }

    public String getFav_pubDate() {
        return fav_pubDate;
    }

    public void setFav_pubDate(String fav_pubDate) {
        this.fav_pubDate = fav_pubDate;
    }

    public String getFav_description() {
        return fav_description;
    }

    public void setFav_description(String fav_description) {
        this.fav_description = fav_description;
    }

    public String getFav_webUrl() {
        return fav_webUrl;
    }

    public void setFav_webUrl(String fav_webUrl) {
        this.fav_webUrl = fav_webUrl;
    }

}
