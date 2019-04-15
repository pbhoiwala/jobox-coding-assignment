package com.jobox.coding.assignment;


import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;

public class News extends RealmObject {

    private String author;
    private String title;
    private String summary;
    private String url;
    private String  imageUrl;
    private Date publishedDate;
    private String content;

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
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                this.publishedDate = df.parse(publishedDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
}
