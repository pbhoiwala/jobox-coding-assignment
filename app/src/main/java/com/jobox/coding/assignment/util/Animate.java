package com.jobox.coding.assignment.util;

import android.content.Context;
import android.support.v7.widget.CardView;

public class Animate {

    private Context context;


    public Animate(Context context) {
        this.context = context;
    }

    public void animateCardViewVisibility(CardView button, boolean isScrollingUp) {
        int scale = isScrollingUp ? 1 : 0;
        if (button.getScaleX() != scale) {
            button.animate()
                    .scaleX(scale)
                    .scaleY(scale)
                    .setDuration(200)
                    .start();
        }
    }

}
