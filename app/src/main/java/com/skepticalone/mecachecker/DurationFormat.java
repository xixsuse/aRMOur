package com.skepticalone.mecachecker;

import android.content.Context;

public final class DurationFormat {

    public static String getDurationString(Context context, long durationInMillis) {
        int hours = (int) (durationInMillis / AppConstants.MILLIS_PER_HOUR);
        int minutes = (int) (durationInMillis % AppConstants.MILLIS_PER_HOUR / AppConstants.MILLIS_PER_MINUTE);
        if (hours == 0) {
            return context.getResources().getQuantityString(R.plurals.minutes, minutes, minutes);
        } else if (minutes == 0) {
            return context.getResources().getQuantityString(R.plurals.hours, hours, hours);
        } else {
            return context.getString(
                    R.string.hours_and_minutes,
                    context.getResources().getQuantityString(R.plurals.hours, hours, hours),
                    context.getResources().getQuantityString(R.plurals.minutes, minutes, minutes)
            );
        }
    }
}
