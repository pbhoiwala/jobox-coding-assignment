package com.jobox.coding.assignment.type;


import com.google.gson.JsonObject;
import com.jobox.coding.assignment.util.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import io.realm.RealmObject;

public class News extends RealmObject {

    private String author;
    private String title;
    private String summary;
    private String url;
    private String  imageUrl;
    private Date publishedDate;
    private String content;

    private int viewType;

    public News() { }

    public News(JsonObject jsonNews) {

        if (!jsonNews.get("author").isJsonNull()) {
            this.author = jsonNews.get("author").getAsString();
        }

        if (!jsonNews.get("title").isJsonNull()) {
            this.title = jsonNews.get("title").getAsString();
        }

        if (!jsonNews.get("description").isJsonNull()) {
            this.summary = jsonNews.get("description").getAsString();
        }

        if (!jsonNews.get("url").isJsonNull()) {
            this.url = jsonNews.get("url").getAsString();
        }

        if (!jsonNews.get("urlToImage").isJsonNull()) {
            this.imageUrl = jsonNews.get("urlToImage").getAsString();
        }

        if (!jsonNews.get("content").isJsonNull()) {
            this.content = jsonNews.get("content").getAsString();
        }

        if (!jsonNews.get("publishedAt").isJsonNull()) {
            String publishedDateString = jsonNews.get("publishedAt").getAsString();
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                this.publishedDate = df.parse(publishedDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        viewType = (int) (Math.random() * 3);

    }

    public String getAuthor() {
        return author != null && !author.isEmpty() ? author : "No Author";
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublishedDate() {
        return publishedDate != null ? Util.getFancyDateDifferenceString(publishedDate.getTime()) : "No Date";
    }

    public String getContent() {
        return content;
    }

    public int getViewType() {
        return viewType;
    }
}
