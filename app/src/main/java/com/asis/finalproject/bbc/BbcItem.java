package com.asis.finalproject.bbc;

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

    /**
     * Constructor
     */
    public BbcItem(){
    }

    /**
     * Overloaded Constructor
     * @param id is the identifier of the article (row)
     * @param title the title of the article loaded from the Internet
     * @param pubDate date of the article release
     * @param description short description of the loaded article
     * @param webUrl  the web link of the article
     */
    public BbcItem(int id, String title, String pubDate, String description, String webUrl) {
        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.webUrl = webUrl;
    }

    /**
     * Overloaded Constructor
     * @param title the title of the article loaded from the Internet
     * @param pubDate  date of the article release
     * @param description short description of the loaded article
     * @param webUrl the web link of the article
     */
    public BbcItem(String title, String pubDate, String description, String webUrl) {
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.webUrl = webUrl;
    }

    /**
     * Method for getting id
     * @return id of the article
     */
    public int getId() {
        return id;
    }

    /**
     * Method for setting Id
     * @param id article's id
     */
    public void setId( int id) {
        this.id = id;
    }

    /**
     * Method for getting title of the article
     * @return the title of article
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method for setting the title
     * @param title the title for article
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method for getting the date of the article release
     * @return the date of article release
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * Method for setting the date when the article released
     * @param pubDate the release date of the article
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * Method for getting description of the article
     * @return description of the article
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method for setting the description
     * @param description description of the article
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method for getting the article's web link
     * @return the web link to the article
     */
    public String getWebUrl() {
        return webUrl;
    }


    /**
     * Method for setting the webLink
     * @param webUrl the web link to the article
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

}
