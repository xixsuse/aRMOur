package com.skepticalone.mecachecker.shift;

import com.skepticalone.mecachecker.AppConstants;
import com.skepticalone.mecachecker.CheckedShift;

import java.util.Date;

class ResponsiveShift extends CheckedShift {
    private final ShiftDisplayListener mListener;

    ResponsiveShift(ShiftDisplayListener listener, long startSeconds, long endSeconds) {
        super(startSeconds, endSeconds);
        mListener = listener;
        updateListenerDate();
        updateListenerStartTime();
        updateListenerEndTime();
        updateListenerDurationAll();
    }

    private static int getDurationInSteps(long duration) {
        return (int) (duration / AppConstants.MILLIS_PER_STEP);
    }

    private void updateListenerDate() {
        mListener.updateDate(getStart());
    }

    private void updateListenerStartTime() {
        mListener.updateStart(getStart());
    }

    private void updateListenerEndTime() {
        mListener.updateEnd(getEnd(), isSameDay());
    }

    private void updateListenerDurationAll() {
        updateListenerDurationBarMax(getDurationInSteps(getMaxDuration()));
        int currentDurationInSteps = getDurationInSteps(getDuration());
        updateListenerDuration(currentDurationInSteps);
        updateListenerDurationBar(currentDurationInSteps);
    }

    private void updateListenerDurationBar(int steps) {
        mListener.updateDurationBarProgress(steps);
    }

    private void updateListenerDuration(int steps) {
        int hours = steps / AppConstants.STEPS_PER_HOUR;
        int minutes = steps % AppConstants.STEPS_PER_HOUR * AppConstants.MINUTES_PER_STEP;
        mListener.updateDuration(hours, minutes);
    }

    private void updateListenerDurationBarMax(int steps) {
        mListener.updateDurationBarMax(steps);
    }

    void onDateUpdatedByUser(int year, int month, int dayOfMonth) {
        updateDate(year, month, dayOfMonth);
        updateListenerDate();
        updateListenerDurationAll();
    }

    void onStartUpdatedByUser(int hourOfDay, int minute) {
        updateStart(hourOfDay, minute);
        updateListenerStartTime();
        updateListenerDurationAll();
    }

    void onEndUpdatedByUser(int hourOfDay, int minute) {
        updateEnd(hourOfDay, minute);
        updateListenerEndTime();
        int currentDurationInSteps = getDurationInSteps(getDuration());
        updateListenerDuration(currentDurationInSteps);
        updateListenerDurationBar(currentDurationInSteps);
    }

    void onDurationUpdatedByUser(int steps) {
        updateEnd(steps * AppConstants.MILLIS_PER_STEP);
        updateListenerEndTime();
        updateListenerDuration(steps);
    }

    interface ShiftDisplayListener {
        void updateDate(Date date);

        void updateStart(Date start);

        void updateEnd(Date end, boolean sameDay);

        void updateDuration(int hours, int minutes);

        void updateDurationBarProgress(int steps);

        void updateDurationBarMax(int steps);
    }
}