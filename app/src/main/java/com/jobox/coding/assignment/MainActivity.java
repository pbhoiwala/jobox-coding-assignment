package com.jobox.coding.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jobox.coding.assignment.callback.OnNewsFetchCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fetcher fetcher = new Fetcher(MainActivity.this);
        final CacheController cacheController = CacheController.getInstance(MainActivity.this);

        fetcher.fetchCurrentNews(new OnNewsFetchCallback() {
            @Override
            public void onSuccess(ArrayList<News> newsList) {
                cacheController.cacheNews(newsList);
                ArrayList<News> cached = cacheController.loadCachedNews();
                cached.isEmpty();
            }

            @Override
            public void onFailure() {

            }
        });




    }



}
