package com.asis.finalproject;

/**
 * BbcArticles class
 * A common object that will hold data
 */
public class BbcArticles {
    private int id;
    private String title;
    private String pubDate;
    private String description;
    private String webUrl;

    /**
     * Constructor
     * @param title
     * @param pubDate
     * @param description
     * @param webUrl
     */
    public BbcArticles(String title, String pubDate, String description, String webUrl) {
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.webUrl = webUrl;
    }

    /**
     * Overloaded constructor
     * @param id
     * @param title
     * @param pubDate
     * @param description
     * @param webUrl
     */
    public BbcArticles(int id, String title, String pubDate, String description, String webUrl) {

        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.webUrl = webUrl;
    }

    public void changeTitle(String text){
        title = text;
    }

    /**
     * Method for setting the title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method for setting the date when the article released
     * @param pubDate
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * Method for setting the description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method for setting the webLink
     * @param webUrl
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    /**
     * Method for getting Id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Method for setting Id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method for getting title of the article
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method for getting the date of the article release
     * @return
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * Method for getting description of the article
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method for getting the article's web link
     * @return
     */
    public String getWebUrl() {
        return webUrl;
    }

}
