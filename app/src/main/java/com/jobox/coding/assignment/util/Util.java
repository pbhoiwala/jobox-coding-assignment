package com.jobox.coding.assignment.util;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;

import java.util.Calendar;
import java.util.Locale;

public class Util {

    public static float scale;
    public static float screenHeight;
    public static float screenWidth;

    public static final int SECONDS_UNIT = 60;
    public static final int MINUTES_UNIT = 60;
    public static final int HOURS_UNIT = 24;
    public static final int DAYS_UNIT = 24 * 7;
    public static final int YEARS_UNIT = 365;

    public Util(Context context) {
        scale = context.getResources().getDisplayMetrics().density;
        screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * Takes in the time of the post and creates a fancy string difference
     * Examples:
     * 10 seconds ago/Just now      (time < minute)
     * 20 minutes ago               (time < hour)
     * 2 hours ago                  (time < day)
     * 4 days ago                   (time < week)
     * January 21                   (time < year)
     * September 18, 2017           (else)
     */
    public static String getFancyDateDifferenceString(long time) {
        if (time < 0) {
            time *= -1;
        }
        // Create a calendar object and calculate the timeFromStart
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        long timeFromCurrent = calendar.getTimeInMillis() - time;

        // Set the calendar object to be the time of the post
        calendar.setTimeInMillis(time);

        // Calculate all units for the given timeFromCurrent
        long secondsTime = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(timeFromCurrent);
        long minutesTime = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(timeFromCurrent);
        long hoursTime = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(timeFromCurrent);
        long daysTime = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(timeFromCurrent);

        // The fancyDateString will start off as this DateFormat to satisfy the else case
        String fancyDateString = DateFormat.format("MMM dd, yyyy", calendar).toString();

        // Check each calculated time unit until it is clear the unit of timeFromCurrent
        if (secondsTime < SECONDS_UNIT) {
//                String fancyDateTail = secondsTime == 1 ? " second ago" : " seconds ago";
//                fancyDateString = secondsTime + fancyDateTail;
            fancyDateString = "Just now";
        } else if (minutesTime < MINUTES_UNIT) {
            String fancyDateTail = minutesTime == 1 ? " minute ago" : " minutes ago";
            fancyDateString = minutesTime + fancyDateTail;
        } else if (hoursTime < HOURS_UNIT) {
            String fancyDateTail = hoursTime == 1 ? " hour ago" : " hours ago";
            fancyDateString = hoursTime + fancyDateTail;
        } else if (daysTime < 7) {
            String fancyDateTail = daysTime == 1 ? " day ago" : " days ago";
            fancyDateString = daysTime + fancyDateTail;
        } else if (daysTime < 30) {
            String fancyDateTail = (daysTime / 7) == 1 ? " week ago" : " weeks ago";
            fancyDateString = (daysTime / 7) + fancyDateTail;
        } else if (daysTime < 365) {
            String fancyDateTail = (daysTime / 30) == 1 ? " month ago" : " months ago";
            fancyDateString = (daysTime / 30) + fancyDateTail;
        } else {
            String fancyDateTail = (daysTime / 365) == 1 ? " year ago" : " years ago";
            fancyDateString = (daysTime / 365) + fancyDateTail;
        }
        return fancyDateString;
    }


}
