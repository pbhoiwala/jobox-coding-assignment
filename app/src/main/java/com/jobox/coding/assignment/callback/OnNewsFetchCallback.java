package com.jobox.coding.assignment.callback;

import com.jobox.coding.assignment.type.News;

import java.util.ArrayList;

public interface OnNewsFetchCallback {
    void onSuccess(ArrayList<News> newsList);
    void onFailure();
}
