package com.asis.finalproject.guardian;

/**
 * @author Naimul Rahman
 * @class Article.java
 * @version 3
 * This class represents articles found using the Guardian's api. Stores the title, url,
 * and section name
 */

public class Article {

    private long id;
    private String title;
    private String url;
    private String sectionName;

    /**
     * Default constructor. Chains to the second constructor if an ID is not provided
     * @param title The article's title
     * @param url The article's url
     * @param sectionName The article's section
     */
    public Article(String title, String url, String sectionName){this(title, url, sectionName, 0);}

    /**
     * @param title The article's title
     * @param url The article's url
     * @param sectionName The article's section
     * @param id An id for the article, given 0 if none is provided. If the article is inserted into the database, it will increment values by 1
     */
    public Article(String title, String url, String sectionName, long id){
        setId(id);
        setTitle(title);
        setUrl(url);
        setSectionName(sectionName);
    }

    /**
     *  Gets the article's ID
     * @return The id of the article
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the article's ID
     * @param id An ID number for the article
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the article's title
     * @return The title of the article
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the article's title
     * @param title The title of the article
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the url for the article
     * @return The article's url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url for the article
     * @return The article's url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the section name for the article
     * @return The article's section name
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the section name for the article
     * @return The article's section name
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }


}
