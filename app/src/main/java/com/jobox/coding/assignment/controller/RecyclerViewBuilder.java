package com.jobox.coding.assignment.controller;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jobox.coding.assignment.R;

public class RecyclerViewBuilder {

    private Context context;

    public RecyclerViewBuilder(Context context) {
        this.context = context;
    }

    public void setupVerticalRecyclerView(RecyclerView recyclerView, int orientation, boolean hasDivider) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setAutoMeasureEnabled(false);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, orientation);
        int divider = hasDivider ? R.drawable.divider : R.drawable.no_divider;
        dividerItemDecoration.setDrawable(context.getResources().getDrawable(divider));

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(defaultItemAnimator);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(false);
    }


}
