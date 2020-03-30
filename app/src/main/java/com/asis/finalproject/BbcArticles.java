package com.asis.finalproject;

public class BbcArticles {
    private int id;
    private String title;
    private String pubDate;
    private String description;
    private String webUrl;

    public BbcArticles(String title, String pubDate, String description, String webUrl) {
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.webUrl = webUrl;
    }

    public BbcArticles(int id, String title, String pubDate, String description, String webUrl) {

        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.webUrl = webUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getWebUrl() {
        return webUrl;
    }


}
