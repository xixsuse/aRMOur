package com.skepticalone.mecachecker.shift;

import java.util.Date;

class Shift extends com.skepticalone.mecachecker.Shift {
    static final int MINUTES_PER_STEP = 5;
    private static final int MILLIS_PER_MINUTE = 1000 * 60;
    private static final int MILLIS_PER_STEP = MILLIS_PER_MINUTE * MINUTES_PER_STEP;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int STEPS_PER_HOUR = MINUTES_PER_HOUR / MINUTES_PER_STEP;
    private final ShiftDisplayListener mListener;

    Shift(ShiftDisplayListener listener, long startSeconds, long endSeconds) {
        super(startSeconds, endSeconds);
        mListener = listener;
        updateListenerDate();
        updateListenerStartTime();
        updateListenerEndTime();
        updateListenerDurationAll();
    }

    private static int getDurationInSteps(long duration) {
        return (int) (duration / MILLIS_PER_STEP);
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
        int currentDurationInSteps = getDurationInSteps(getCurrentDuration());
        updateListenerDuration(currentDurationInSteps);
        updateListenerDurationBar(currentDurationInSteps);
    }

    private void updateListenerDurationBar(int steps) {
        mListener.updateDurationBarProgress(steps);
    }

    private void updateListenerDuration(int steps) {
        int hours = steps / STEPS_PER_HOUR;
        int minutes = steps % STEPS_PER_HOUR * MINUTES_PER_STEP;
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
        int currentDurationInSteps = getDurationInSteps(getCurrentDuration());
        updateListenerDuration(currentDurationInSteps);
        updateListenerDurationBar(currentDurationInSteps);
    }

    void onDurationUpdatedByUser(int steps) {
        updateEnd(steps * MILLIS_PER_STEP);
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