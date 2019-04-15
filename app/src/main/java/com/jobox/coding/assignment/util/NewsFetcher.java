package com.jobox.coding.assignment.util;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jobox.coding.assignment.R;
import com.jobox.coding.assignment.async.AsyncAction;
import com.jobox.coding.assignment.callback.OnNewsFetchCallback;
import com.jobox.coding.assignment.type.News;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NewsFetcher {

    private static String QUERY_START = "https://newsapi.org/v2/everything?q=ai";
    private static String QUERY_LANG= "&language=en";
    private static String QUERY_FROM = "&from=";
    private static String QUERY_TO = "&to=";
    private static String QUERY_END = "&sortBy=publishedAt";
    private static String QUERY_API = "&apiKey=";

    private Context context;
    private Calendar calendar;

    public NewsFetcher(Context context) {
        this.context = context;
        this.calendar = Calendar.getInstance();
    }

    public void fetchCurrentNews(final OnNewsFetchCallback callback){
        calendar = Calendar.getInstance();
        String query = buildQuery(calendar);
        new AsyncAction.FetchNews().execute(context, query, new ArrayList<>(), callback);
    }

    public void fetchMoreNews(final OnNewsFetchCallback callback) {
        calendar.add(Calendar.DATE, -1);
        String query = buildQuery(calendar);
        new AsyncAction.FetchNews().execute(context, query, new ArrayList<>(), callback);
    }

    public ArrayList<News> fetchNews(String query) {
        final ArrayList<News> newsList = new ArrayList<>();

        try {
            String json = readUrl(query);
            JsonObject result = new Gson().fromJson(json, JsonObject.class);
            for (JsonElement jsonNews : result.getAsJsonArray("articles")) {
                News news = new News(jsonNews.getAsJsonObject());
                newsList.add(news);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;

//        Ion.with(context)
//                .load(query)
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        for (JsonElement jsonNews : result.getAsJsonArray("articles")) {
//                            News news = new News(jsonNews.getAsJsonObject());
//                            newsList.add(news);
//                        }
//                        callback.onSuccess(newsList);
//                    }
//                });

    }

    private String readUrl(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return "";
    }

    private String buildQuery(Calendar date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String day = df.format(date.getTime());

        String query = QUERY_START +
                QUERY_LANG +
                QUERY_FROM + day +
                QUERY_TO + day +
                QUERY_END +
                QUERY_API + context.getString(R.string.news_api_key);

        return query;

    }



}
