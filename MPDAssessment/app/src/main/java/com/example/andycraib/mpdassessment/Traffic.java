package com.example.andycraib.mpdassessment;

import java.io.Serializable;

// Andrew Craib S1628364

public class Traffic implements Serializable
{
    //Variables declared
    private String title;
    private String description;
    private String link;
    private String georss;
    private String author;
    private String comments;
    private String pubDate;

    //Initialising Variables
    public Traffic()
    {
        title = "";
        description = "";
        link = "";
        georss = "";
        author = "";
        comments = "";
        pubDate = "";
    }

    public Traffic(String date)
    {
        description = date;
    }

    //Constructor
    public Traffic(String ttitle , String tdescription, String tlink, String tgeorss, String tauthor, String tcomments, String tpubDate)
    {
        title = ttitle;
        description = tdescription;
        link = tlink;
        georss = tgeorss;
        author = tauthor;
        comments = tcomments;
        pubDate = tpubDate;
    }

    //Overload Constructor
    public Traffic(String ttitle , String tdescription, String tlink)
    {
        title = ttitle;
        description = tdescription;
        link = tlink;
    }

    //Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

}
