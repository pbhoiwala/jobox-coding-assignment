package com.jobox.coding.assignment;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jobox.coding.assignment.callback.OnNewsFetchCallback;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Fetcher {

    private static String QUERY_START = "https://newsapi.org/v2/everything?q=ai";
    private static String QUERY_LANG= "&language=en";
    private static String QUERY_FROM = "&from=";
    private static String QUERY_TO = "&to=";
    private static String QUERY_END = "&sortBy=publishedAt";
    private static String QUERY_API = "&apiKey=";

    private Context context;
    private Calendar calendar;

    public Fetcher(Context context) {
        this.context = context;
        this.calendar = Calendar.getInstance();
    }

    public void fetchCurrentNews(final OnNewsFetchCallback callback){
        calendar = Calendar.getInstance();
        String query = buildQuery(calendar);
        fetchNews(query, callback);
    }

    public void fetchMoreNews(final OnNewsFetchCallback callback) {
        calendar.add(Calendar.DATE, -1);
        String query = buildQuery(calendar);
        fetchNews(query, callback);
    }

    private void fetchNews(String query, final OnNewsFetchCallback callback) {
        final ArrayList<News> newsList = new ArrayList<>();
        Ion.with(context)
                .load(query)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        for (JsonElement jsonNews : result.getAsJsonArray("articles")) {
                            News news = new News(jsonNews.getAsJsonObject());
                            newsList.add(news);
                        }
                        callback.onSuccess(newsList);
                    }
                });
    }

    private String buildQuery(Calendar date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String day = df.format(calendar.getTime());

        String query = QUERY_START +
                QUERY_LANG +
                QUERY_FROM + day +
                QUERY_TO + day +
                QUERY_END +
                QUERY_API + context.getString(R.string.news_api_key);

        return query;

    }

}
