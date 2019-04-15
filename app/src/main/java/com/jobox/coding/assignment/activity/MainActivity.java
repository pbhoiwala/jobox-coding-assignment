package com.jobox.coding.assignment.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.OrientationListener;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jobox.coding.assignment.adapter.NewsRecyclerViewAdapter;
import com.jobox.coding.assignment.controller.CacheController;
import com.jobox.coding.assignment.controller.RecyclerViewBuilder;
import com.jobox.coding.assignment.util.NewsFetcher;
import com.jobox.coding.assignment.type.News;
import com.jobox.coding.assignment.R;
import com.jobox.coding.assignment.callback.OnNewsFetchCallback;
import com.jobox.coding.assignment.util.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CacheController cacheController;
    private NewsFetcher newsFetcher;
    private RecyclerViewBuilder recyclerViewBuilder;

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView titleTextView;
    private NestedScrollView nestedScrollView;

    private ProgressBar progressBar;
    private RecyclerView newsRecyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private ArrayList<News> newsArrayList;

    private boolean shouldLoadMore = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        new Util(this);

        cacheController = CacheController.getInstance(this);
        newsFetcher = new NewsFetcher(this);
        recyclerViewBuilder = new RecyclerViewBuilder(this);

        coordinatorLayout = findViewById(R.id.main_activity_coordinator_layout);
        appBarLayout = findViewById(R.id.main_activity_app_bar_layout);
        toolbar = findViewById(R.id.main_activity_toolbar);
        titleTextView = findViewById(R.id.main_activity_toolbar_title_text_view);
        nestedScrollView = findViewById(R.id.main_activity_nested_scroll_view);

        progressBar = findViewById(R.id.main_activity_recycler_view_progress_bar);
        newsRecyclerView = findViewById(R.id.main_activity_recycler_view);

        newsArrayList = new ArrayList<>();

        setupInterfaceElements();


    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        titleTextView.setText("jobox.ai");
    }

    private void setupNestedScrollView() {
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View v = nestedScrollView.getChildAt(0);
                if (shouldLoadMore && nestedScrollView.getScrollY() >= ((v.getMeasuredHeight() - nestedScrollView.getMeasuredHeight()))) {
                    shouldLoadMore = false;
                    Toast.makeText(MainActivity.this, "End reached", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);

                    //TODO: Fetch more news and set boolean shouldLoadMore back to true
                    fetchMoreNewsArticle();

                }
            }
        });
    }

    private void setupNewsRecyclerView() {
        recyclerViewBuilder.setupVerticalRecyclerView(newsRecyclerView, DividerItemDecoration.VERTICAL, false);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this, newsArrayList);
        newsRecyclerViewAdapter.setHasStableIds(true);
        newsRecyclerView.setAdapter(newsRecyclerViewAdapter);
        fetchNewsArticles();
    }

    private void setupInterfaceElements() {
        setupToolbar();
        setupNestedScrollView();
        setupNewsRecyclerView();
    }

    private void fetchNewsArticles() {
        progressBar.setVisibility(View.VISIBLE);

        newsFetcher.fetchCurrentNews(new OnNewsFetchCallback() {
            @Override
            public void onSuccess(ArrayList<News> newsList) {
                cacheController.cacheNews(newsList);
                newsArrayList.addAll(newsList);
                newsRecyclerViewAdapter.notifyDataSetChanged();
                newsRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                shouldLoadMore = true;
            }

            @Override
            public void onFailure() {
                newsRecyclerViewAdapter.notifyDataSetChanged();
                newsRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void fetchMoreNewsArticle() {

        newsFetcher.fetchMoreNews(new OnNewsFetchCallback() {
            @Override
            public void onSuccess(ArrayList<News> newsList) {
                newsArrayList.addAll(newsList);
                newsRecyclerViewAdapter.notifyItemRangeInserted(newsArrayList.size(), newsList.size());
                newsRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                shouldLoadMore = true;
            }

            @Override
            public void onFailure() {
                newsRecyclerViewAdapter.notifyDataSetChanged();
                newsRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }




}
