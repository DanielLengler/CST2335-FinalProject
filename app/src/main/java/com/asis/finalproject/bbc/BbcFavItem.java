package com.asis.finalproject.bbc;

public class BbcFavItem {
    private int id;
    private String fav_title;
    private String fav_pubDate;
    private String fav_description;
    private String fav_webUrl;

    /**
     * Constructor
     */
    public BbcFavItem(){
    }

    /**
     * Overloaded Constructor
     * @param id is the identifier of the article (row)
     * @param fav_title the title of the favorite article
     * @param fav_pubDate date of the favorite article release
     * @param fav_description short description of the favorite article
     * @param fav_webUrl  the web link of the favorite article
     */
    public BbcFavItem(int id, String fav_title, String fav_pubDate, String fav_description, String fav_webUrl) {
        this.id = id;
        this.fav_title = fav_title;
        this.fav_pubDate = fav_pubDate;
        this.fav_description = fav_description;
        this.fav_webUrl = fav_webUrl;
    }

    /**
     *
     * @param fav_title the title of the favorite article
     * @param fav_pubDate the title of the favorite article
     * @param fav_description date of the favorite article release
     * @param fav_webUrl the web link of the favorite article
     */
    public BbcFavItem(String fav_title, String fav_pubDate, String fav_description, String fav_webUrl) {
        this.fav_title = fav_title;
        this.fav_pubDate = fav_pubDate;
        this.fav_description = fav_description;
        this.fav_webUrl = fav_webUrl;
    }

    /**
     * Method for getting Id
     * @return id of the article
     */
    public int getId() {
        return id;
    }

    /**
     * Method for setting Id
     * @param id article's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *ethod for getting title of the favorite article
     * @return the title of favorite article
     */
    public String getFav_title() {
        return fav_title;
    }

    /**
     * Method for setting the title of the favorite article
     * @param fav_title  the title for favorite article
     */
    public void setFav_title(String fav_title) {
        this.fav_title = fav_title;
    }

    /**
     * Method for getting the date of the favorite article release
     * @return the date of article release
     */
    public String getFav_pubDate() {
        return fav_pubDate;
    }

    /**
     * Method for setting the date when the article released
     * @param fav_pubDate the release date for the article
     */
    public void setFav_pubDate(String fav_pubDate) {
        this.fav_pubDate = fav_pubDate;
    }

    /**
     * Method for getting description of the article
     * @return description of the article
     */
    public String getFav_description() {
        return fav_description;
    }

    /**
     * Method for setting the description of the article
     * @param fav_description description for the article
     */
    public void setFav_description(String fav_description) {
        this.fav_description = fav_description;
    }

    /**
     * Method for getting the webLink of the article
     * @return the web link to the article
     */
    public String getFav_webUrl() {
        return fav_webUrl;
    }

    /**
     * Method for setting the webLink of the article
     * @param fav_webUrl the web link to the article
     */
    public void setFav_webUrl(String fav_webUrl) {
        this.fav_webUrl = fav_webUrl;
    }

}
