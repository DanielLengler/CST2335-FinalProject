package com.asis.finalproject.nasaimageoftheday;

/**
 * Class creates objects that contains all the data for an image.
 */
public class NasaImageItem {

    private long id;
    private String title;
    private String explanation;
    private String date;
    private String url;
    private String path;

    /**
     * Constructor that create Nasa Image object
     * @param title Title of the image retrieved from Nasa
     * @param explanation Explanation of an image retrieved from Nasa
     * @param date Image date retrieved from Nasa
     * @param url URL address for API data
     * @param path Path of the image into the phone
     * @param id ID of an object into the DB
     */
    protected NasaImageItem(String title, String explanation, String date, String url, String path, long id){
        setDate(date);
        setExplanation(explanation);
        setId(id);
        setPath(path);
        setTitle(title);
        setUrl(url);
    }

    protected long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected String getExplanation() {
        return explanation;
    }

    private void setExplanation(String explanation) {
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

    protected String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
    }
}