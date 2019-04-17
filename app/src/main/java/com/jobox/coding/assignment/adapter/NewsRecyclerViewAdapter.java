package com.jobox.coding.assignment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.jobox.coding.assignment.util.IntentTo;
import com.jobox.coding.assignment.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_1 = 0;
    private final int VIEW_TYPE_2 = 1;
    private final int VIEW_TYPE_3 = 2;

    private IntentTo intentTo;
    private LayoutInflater layoutInflater;

    private Context context;
    private ArrayList<News> newsArrayList;

    public NewsRecyclerViewAdapter(Context context, ArrayList<News> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;

        intentTo = new IntentTo(context);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return newsArrayList.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_TYPE_1:
                view = layoutInflater.inflate(R.layout.news_recycler_view_item_layout_1, parent, false);
                break;
            case VIEW_TYPE_2:
                view = layoutInflater.inflate(R.layout.news_recycler_view_item_layout_2, parent, false);
                break;
            case VIEW_TYPE_3:
                view = layoutInflater.inflate(R.layout.news_recycler_view_item_layout_3, parent, false);
                break;
        }
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NewsViewHolder) holder).setData(newsArrayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;
        private TextView titleTextView;
        private TextView authorDateTextView;
        private TextView summaryTextView;
        private ImageView newsImageView;

        private News news;

        NewsViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.news_recycler_view_item_linear_layout);
            titleTextView = itemView.findViewById(R.id.news_recycler_view_item_title);
            authorDateTextView = itemView.findViewById(R.id.news_recycler_view_item_author_date);
            summaryTextView = itemView.findViewById(R.id.news_recycler_view_item_summary);
            newsImageView = itemView.findViewById(R.id.news_recycler_view_item_image_view);
        }

        private void setData(News news, int position) {
            this.news = news;
            populateInterfaceElements(position);
        }

        private void populateInterfaceElements(final int position) {
            String title = "";
            title += "<b>" + news.getAuthor() + "</b>";
            title += " • " + news.getPublishedDate();

            titleTextView.setText(news.getTitle());
            titleTextView.setSelected(true);

            authorDateTextView.setText(Html.fromHtml(title));
            authorDateTextView.setSelected(true);

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

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentTo.carouselActivity(position);
                }
            });
        }

    }

}