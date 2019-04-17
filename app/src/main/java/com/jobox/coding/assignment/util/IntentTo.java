package com.jobox.coding.assignment.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jobox.coding.assignment.R;
import com.jobox.coding.assignment.activity.CarouselActivity;
import com.jobox.coding.assignment.constant.IntentCode;
import com.jobox.coding.assignment.constant.IntentKey;

public class IntentTo {

    private Context context;


    public IntentTo(Context context) {
        this.context = context;
    }

    /**
     * Performs intent to carouselActivity with given position
     * of the news article that was clicked
     */
    public void carouselActivity(int position) {
        Intent carouselIntent = new Intent(context, CarouselActivity.class);
        carouselIntent.putExtra(IntentKey.NEWS_POSITION, position);
        ((Activity) context).startActivityForResult(carouselIntent, IntentCode.CAROUSEL_INTENT_CODE);
        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
