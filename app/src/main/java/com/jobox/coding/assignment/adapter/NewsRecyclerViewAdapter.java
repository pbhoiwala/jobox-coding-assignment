package com.jobox.coding.assignment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jobox.coding.assignment.R;
import com.jobox.coding.assignment.type.News;

import java.util.ArrayList;


public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;

    private Context context;
    private ArrayList<News> newsArrayList;

    public NewsRecyclerViewAdapter(Context context, ArrayList<News> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.news_recycler_view_item_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NewsViewHolder) holder).setData(newsArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView authorDateTextView;
        private TextView summaryTextView;
        private ImageView newsImageView;

        private News news;

        NewsViewHolder(View itemView) {
            super(itemView);

            authorDateTextView = itemView.findViewById(R.id.news_recycler_view_item_author_date);
            summaryTextView = itemView.findViewById(R.id.news_recycler_view_item_summary);
            newsImageView = itemView.findViewById(R.id.news_recycler_view_item_image_view);
        }

        private void setData(News news) {
            this.news = news;
            populateInterfaceElements();
        }

        private void populateInterfaceElements() {
            String title = "";
            title += "<b>" + news.getAuthor() + "</b>";
            title += " • " + news.getPublishedDate();

            authorDateTextView.setText(Html.fromHtml(title));

            summaryTextView.setText(news.getSummary());

            boolean hasImage = news.getImageUrl() != null && !news.getImageUrl().isEmpty();
            if (hasImage) {
                Glide.with(context)
                        .asBitmap()
                        .load(news.getImageUrl())
                        .apply(new RequestOptions().fitCenter())
                        .into(new BitmapImageViewTarget(newsImageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                newsImageView.setImageBitmap(resource);
                                newsImageView.setVisibility(View.VISIBLE);
                                // TODO Add progress bar
                            }
                });

            } else {
                newsImageView.setVisibility(View.GONE);
            }


        }

    }

}