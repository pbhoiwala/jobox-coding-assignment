package com.jobox.coding.assignment.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jobox.coding.assignment.R;
import com.jobox.coding.assignment.adapter.NewsRecyclerViewAdapter;
import com.jobox.coding.assignment.callback.OnNewsFetchCallback;
import com.jobox.coding.assignment.constant.IntentCode;
import com.jobox.coding.assignment.constant.IntentKey;
import com.jobox.coding.assignment.controller.CacheController;
import com.jobox.coding.assignment.controller.RecyclerViewBuilder;
import com.jobox.coding.assignment.network.NetworkStateReceiver;
import com.jobox.coding.assignment.type.News;
import com.jobox.coding.assignment.util.Animate;
import com.jobox.coding.assignment.util.NewsFetcher;
import com.jobox.coding.assignment.util.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private NetworkStateReceiver networkStateReceiver;

    private Animate animate;
    private CacheController cacheController;
    private NewsFetcher newsFetcher;
    private RecyclerViewBuilder recyclerViewBuilder;

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView titleTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private CardView goToTopCardView;
    private LinearLayout goToTopButtonLinearLayout;

    private ProgressBar progressBar;
    private RecyclerView newsRecyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private ArrayList<News> newsArrayList;

    private boolean shouldLoadMore = false;
    private boolean networkInterrupted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        new Util(this);

        setupNetworkListener();

        animate = new Animate(this);
        cacheController = CacheController.getInstance(this);
        newsFetcher = new NewsFetcher(this);
        recyclerViewBuilder = new RecyclerViewBuilder(this);

        appBarLayout = findViewById(R.id.main_activity_app_bar_layout);
        toolbar = findViewById(R.id.main_activity_toolbar);
        titleTextView = findViewById(R.id.main_activity_toolbar_title_text_view);
        swipeRefreshLayout = findViewById(R.id.main_activity_swipe_refresh_layout);
        nestedScrollView = findViewById(R.id.main_activity_nested_scroll_view);
        goToTopCardView = findViewById(R.id.main_activity_go_to_top_card_view);
        goToTopButtonLinearLayout = findViewById(R.id.main_activity_go_to_top_button_linear_layout);

        progressBar = findViewById(R.id.main_activity_recycler_view_progress_bar);
        newsRecyclerView = findViewById(R.id.main_activity_recycler_view);

        newsArrayList = new ArrayList<>();

        setupInterfaceElements();
    }

    /**
     * If user has scrolled down, scroll to the top when they press
     * the back button otherwise perform normal backPress operation
     */
    @Override
    public void onBackPressed() {
        if (nestedScrollView.getScrollY() > 0) {
            appBarLayout.setExpanded(true, true);
            nestedScrollView.smoothScrollTo(0, 0);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Used when Carousel activity returns the position of the
     * news article that was last seen
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.CAROUSEL_INTENT_CODE) {
            if (resultCode == RESULT_OK) {
                int resultNewsPosition = data.getIntExtra(IntentKey.RESULT_NEWS_POSITION, 0);
                RecyclerView.LayoutManager layoutManager = newsRecyclerView.getLayoutManager();
                if (layoutManager != null) {
                    View view = layoutManager.findViewByPosition(resultNewsPosition);
                    if (view != null) {
                        float y = view.getY();
                        nestedScrollView.scrollTo(0, (int) y);
                    }
                }

            }
        }
    }

    /**
     * Sets up the toolbar and attaches a listener to the appbarLayout.
     * The listener controls when the "goToTop" buttons hows and hides.
     * When the toolbar is visible, the "goToTop" button is visible
     * and hides otherwise.
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        titleTextView.setText("News List");

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                animate.animateCardViewVisibility(goToTopCardView, nestedScrollView.getScrollY() != 0 && i == 0);
            }
        });
    }

    /**
     * Sets up a listener for the nestedScrollView. When the user
     * scrolls all the way down, more news articles are fetched.
     * Also the "goToTop" button is shows when the user scrolls
     * in the up directions and hides when scroll is downwards
     */
    private void setupNestedScrollView() {
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View v = nestedScrollView.getChildAt(0);

                if (nestedScrollView.getScrollY() == 0) {
                    animate.animateCardViewVisibility(goToTopCardView, false);
                }

                if (shouldLoadMore && nestedScrollView.getScrollY() >= ((v.getMeasuredHeight() - nestedScrollView.getMeasuredHeight()))) {
                    shouldLoadMore = false;
//                    Toast.makeText(MainActivity.this, "Fetching more news...", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);

                    fetchMoreNewsArticle();
                }
            }
        });
    }

    /**
     * Sets up the newsRecyclerView. The handler calls notifyDatasetChanged()
     * every 60 seconds, so the timestamps on every card stays updated
     */
    private void setupNewsRecyclerView() {
        recyclerViewBuilder.setupVerticalRecyclerView(newsRecyclerView, DividerItemDecoration.VERTICAL, false);
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this, newsArrayList);
        newsRecyclerViewAdapter.setHasStableIds(true);
        newsRecyclerView.setAdapter(newsRecyclerViewAdapter);
        fetchNewsArticles();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                newsRecyclerViewAdapter.notifyDataSetChanged();
            }
        }, 60000);
    }

    /**
     * User can drag down to refresh. Upon refresh, old cache
     * is cleared, new data is fetched again and cached
     */
    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setColorScheme(Util.colors);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shouldLoadMore = false;
                newsArrayList.clear();
                newsRecyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                fetchNewsArticles();
            }
        });
    }

    /**
     * Takes the user to top of the page
     */
    private void setupGoToTopButtonLinearLayout() {
        goToTopButtonLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate.animateCardViewVisibility(goToTopCardView, false);
                appBarLayout.setExpanded(true, true);
                nestedScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    /**
     * Sets up all of the interface functions above
     */
    private void setupInterfaceElements() {
        setupToolbar();
        setupNestedScrollView();
        setupNewsRecyclerView();
        setupSwipeRefreshLayout();
        setupGoToTopButtonLinearLayout();
    }

    /**
     * Checks to see if internet is available or not. If not,
     * cached data is loaded if available. If internet is
     * available, new data is fetched and cached
     */
    private void fetchNewsArticles() {
        if (!Util.isNetworkAvailable(this)) {
            newsArrayList.addAll(cacheController.loadCachedNews());
            newsRecyclerViewAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            shouldLoadMore = false;
            if (newsArrayList.isEmpty()) {
                Toast.makeText(this, "No cached data", Toast.LENGTH_SHORT).show();
            }
        } else {
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
    }

    /**
     * Fetches more news articles and adds them to
     * the cached database
     */
    private void fetchMoreNewsArticle() {
        newsFetcher.fetchMoreNews(new OnNewsFetchCallback() {
            @Override
            public void onSuccess(ArrayList<News> newsList) {
                cacheController.cacheMoreNews(newsList);
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

    /**
     * Registers network listener
     */
    private void setupNetworkListener() {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * If network is available and if it was interrupted before,
     * it fetches the data if user clicks "Refresh" on the snackbar
     */
    @Override
    public void onNetworkConnected() {
        if (networkInterrupted) {
            CoordinatorLayout parentLayout = findViewById(R.id.main_activity_coordinator_layout);
            Snackbar.make(parentLayout, "Network available", Snackbar.LENGTH_LONG)
                    .setAction("REFRESH", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fetchNewsArticles();
                            Toast.makeText(MainActivity.this, "News successfully refreshed", Toast.LENGTH_SHORT).show();
                            appBarLayout.setExpanded(true, true);
                            nestedScrollView.smoothScrollTo(0,0);
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                    .show();
        }
    }

    /**
     * Displays a snackbar to the user notifying the user about
     * the network interruption
     */
    @Override
    public void onNetworkDisconnected() {
        CoordinatorLayout parentLayout = findViewById(R.id.main_activity_coordinator_layout);
        Snackbar.make(parentLayout, "Network unavailable", Snackbar.LENGTH_INDEFINITE)
                .show();
        networkInterrupted = true;
    }

    /**
     * Unregisters the newtwork listener
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }
}
