package com.jobox.coding.assignment.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.OrientationListener;
import android.widget.LinearLayout;

import com.jobox.coding.assignment.adapter.NewsRecyclerViewAdapter;
import com.jobox.coding.assignment.controller.CacheController;
import com.jobox.coding.assignment.controller.RecyclerViewBuilder;
import com.jobox.coding.assignment.util.NewsFetcher;
import com.jobox.coding.assignment.type.News;
import com.jobox.coding.assignment.R;
import com.jobox.coding.assignment.callback.OnNewsFetchCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CacheController cacheController;
    private NewsFetcher newsFetcher;
    private RecyclerViewBuilder recyclerViewBuilder;

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    private RecyclerView newsRecyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private ArrayList<News> newsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        cacheController = CacheController.getInstance(this);
        newsFetcher = new NewsFetcher(this);
        recyclerViewBuilder = new RecyclerViewBuilder(this);

        coordinatorLayout = findViewById(R.id.main_activity_coordinator_layout);
        appBarLayout = findViewById(R.id.main_activity_app_bar_layout);
        toolbar = findViewById(R.id.main_activity_toolbar);
        newsRecyclerView = findViewById(R.id.main_activity_recycler_view);

        newsArrayList = new ArrayList<>();

        setSupportActionBar(toolbar);

        recyclerViewBuilder.setupVerticalRecyclerView(newsRecyclerView, DividerItemDecoration.VERTICAL, true);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this, newsArrayList);
        newsRecyclerView.setAdapter(newsRecyclerViewAdapter);

        newsFetcher.fetchCurrentNews(new OnNewsFetchCallback() {
            @Override
            public void onSuccess(ArrayList<News> newsList) {
                cacheController.cacheNews(newsList);
                newsArrayList.addAll(newsList);
                newsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });




    }



}
