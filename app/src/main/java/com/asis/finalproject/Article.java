package com.asis.finalproject;

public class Article {

    private long id;
    private String title;
    private String url;
    private String sectionName;

    public Article(String title, String url, String sectionName){this(title, url, sectionName, 0);}

    public Article(String title, String url, String sectionName, long id){
        setId(id);
        setTitle(title);
        setUrl(url);
        setSectionName(sectionName);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }


}
