package com.asis.finalproject;

/**
 * BbcItem class
 * A common object that will hold data
 */
public class BbcItem {
    private int id;
    private String title;
    private String pubDate;
    private String description;
    private String webUrl;
    private String favStatus;

    public BbcItem(){
    }

    /**
     * Constructor
     * @param title
     * @param pubDate
     * @param description
     * @param webUrl
     */
    public BbcItem(int id, String title, String pubDate, String description, String webUrl, String favStatus) {
        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.webUrl = webUrl;
        this.favStatus = favStatus;
    }

    public BbcItem(String title, String pubDate, String description, String webUrl) {
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.webUrl = webUrl;
    }

    /**
     * Method for getting id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Method for setting Id
     * @param id
     */
    public void setId( int id) {
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
     * Method for setting the title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method for getting the date of the article release
     * @return
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * Method for setting the date when the article released
     * @param pubDate
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * Method for getting description of the article
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method for setting the description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method for getting the article's web link
     * @return
     */
    public String getWebUrl() {
        return webUrl;
    }


    /**
     * Method for setting the webLink
     * @param webUrl
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }


    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

}
