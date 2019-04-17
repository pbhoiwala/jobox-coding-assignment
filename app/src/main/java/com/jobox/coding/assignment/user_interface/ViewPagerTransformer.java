package com.jobox.coding.assignment.user_interface;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.jobox.coding.assignment.util.Util;

public class ViewPagerTransformer implements ViewPager.PageTransformer {

    private static final float MAX_SCALE = 0.95f;
    private static final float MIN_SCALE = 0.8f;
    private static final float MIN_ELEVATION = 2 * Util.scale;
    private static final float MAX_ELEVATION = 6 * Util.scale;


    @Override
    public void transformPage(View page, float position) {
        if (position < -1) {
            // [-Infinity,-1)
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);

        } else if (position <= 1) {
            // [-1,1]
            float val = Math.min(MAX_SCALE, 1 - Math.abs(position));
            if (val >= MIN_SCALE) {
                page.setScaleX(val);
                page.setScaleY(val);
            }

        } else {
            // (1, +Infinity]
            // This page is way off-screen to the right.
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }
    }
}
