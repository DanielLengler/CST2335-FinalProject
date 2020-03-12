package com.asis.finalproject;

public class NasaImageItem {

    private long id;
    private String title;
    private String explanation;
    private String date;
    private String url;
    private String path;

    public NasaImageItem(String title, String explanation, String date, String url, String path, long id){
        setDate(date);
        setExplanation(explanation);
        setId(id);
        setPath(path);
        setTitle(title);
        setUrl(url);
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

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}