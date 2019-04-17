package com.jobox.coding.assignment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.jobox.coding.assignment.R;
import com.jobox.coding.assignment.adapter.NewsViewPagerAdapter;
import com.jobox.coding.assignment.constant.IntentKey;
import com.jobox.coding.assignment.controller.CacheController;
import com.jobox.coding.assignment.type.News;
import com.jobox.coding.assignment.user_interface.ViewPagerTransformer;
import com.jobox.coding.assignment.util.Util;

import java.util.ArrayList;

public class CarouselActivity extends AppCompatActivity {

    private CacheController cacheController;

    private Toolbar toolbar;
    private TextView titleTextView;

    private NewsViewPagerAdapter newsViewPagerAdapter;
    private ViewPager newsViewPager;
    private ArrayList<News> newsArrayList;

    private int currentNewsPosition = 0;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carousel_activity_layout);

        cacheController = CacheController.getInstance(this);

        toolbar = findViewById(R.id.carousel_activity_toolbar);
        titleTextView = findViewById(R.id.carousel_activity_toolbar_title_text_view);
        newsViewPager = findViewById(R.id.carousel_activity_view_pager);

        newsArrayList = new ArrayList<>(cacheController.loadCachedNews());

        setupInterfaceElements();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(IntentKey.RESULT_NEWS_POSITION, newsViewPager.getCurrentItem());
        setResult(RESULT_OK, data);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void getIntentData() {
        currentNewsPosition = getIntent().getIntExtra(IntentKey.NEWS_POSITION, 0);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        titleTextView.setText("Carousel");
    }

    private void setupNewsViewPager() {
        newsViewPagerAdapter = new NewsViewPagerAdapter(this, newsArrayList);

        ViewPagerTransformer viewPagerTransformer = new ViewPagerTransformer();

        newsViewPager.setAdapter(newsViewPagerAdapter);
        newsViewPager.setPageTransformer(false, viewPagerTransformer);
        newsViewPager.setPageMargin((int) (24 * Util.scale));
        newsViewPager.setOffscreenPageLimit(5);

        newsViewPager.setCurrentItem(currentNewsPosition, false);
    }

    private void setupInterfaceElements() {
        getIntentData();
        setupToolbar();
        setupNewsViewPager();
    }
}
