package com.jobox.coding.assignment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jobox.coding.assignment.R;
import com.jobox.coding.assignment.async.AsyncAction;
import com.jobox.coding.assignment.callback.OnCompressBitmapCallback;
import com.jobox.coding.assignment.type.News;
import com.jobox.coding.assignment.util.Util;

import java.util.ArrayList;


public class NewsViewPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;

    private Context context;
    private ArrayList<News> newsArrayList;
    private ArrayList<CardView> cardViewArrayList;


    public NewsViewPagerAdapter(Context context, ArrayList<News> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;

        cardViewArrayList = new ArrayList<>();

        for (int i = 0; i < newsArrayList.size(); i++) {
            cardViewArrayList.add(i, new CardView(context));
        }

        layoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {
        News news = newsArrayList.get(position);


        View view = layoutInflater.inflate(R.layout.news_view_pager_item_layout, collection, false);

        CardView cardView = view.findViewById(R.id.news_view_pager_item_card_view);
        cardViewArrayList.add(position, cardView);

        LinearLayout linearLayout = view.findViewById(R.id.news_view_pager_item_linear_layout);
        TextView titleTextView = view.findViewById(R.id.news_view_pager_item_title);
        TextView authorDateTextView = view.findViewById(R.id.news_view_pager_item_author_date);
        TextView summaryTextView = view.findViewById(R.id.news_view_pager_item_summary);
        final ImageView newsImageView = view.findViewById(R.id.news_view_pager_item_image_view);

        titleTextView.setText(news.getTitle());

        String title = "";
        title += "<b>" + news.getAuthor() + "</b>";
        title += " • " + news.getPublishedDate();

        titleTextView.setText(news.getTitle());
        titleTextView.setSelected(true);

        authorDateTextView.setText(Html.fromHtml(title));
        summaryTextView.setText(news.getSummary());

        boolean hasImage = news.getImageUrl() != null && !news.getImageUrl().isEmpty();
        if (hasImage) {
            Glide.with(context)
                    .asBitmap()
                    .load(news.getImageUrl())
                    .apply(new RequestOptions().centerInside().override((int) (Util.screenWidth * 0.9)))
                    .into(new BitmapImageViewTarget(newsImageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            if (resource != null) {
                                new AsyncAction.CompressBitmap().execute(this, resource, new OnCompressBitmapCallback() {
                                    @Override
                                    public void onSuccess(Bitmap decode) {
                                        newsImageView.setImageBitmap(decode);
                                        newsImageView.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        }
                    });

        } else {
            newsImageView.setVisibility(View.GONE);
        }



        collection.addView(view);
        collection.setTag(news);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return newsArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

}
