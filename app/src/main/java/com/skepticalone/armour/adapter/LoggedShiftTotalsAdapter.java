package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

public final class LoggedShiftTotalsAdapter extends ItemTotalsAdapter<RosteredShift> {

    private static final int
            ROW_NUMBER_LOGGED_SHIFTS = 0,
    // N logged shifts
    ROW_NUMBER_TIME_PERIOD = 1,
    // dd/mm/yy - dd/mm/yy (n days)
    ROW_NUMBER_ROSTERED_HOURS = 2,
    // Rostered: n hours, n minutes
    ROW_NUMBER_LOGGED_HOURS = 3,
    // Logged: n hours, n minutes
    ROW_NUMBER_DIFFERENCE_HOURS = 4,
    // Difference: (+/-) n hours, n minutes
    ROW_COUNT = 5;

    public LoggedShiftTotalsAdapter(@NonNull Context context) {
        super(context, R.string.logged_shift_totals);
    }

    @Override
    int getFixedItemCount() {
        return ROW_COUNT;
    }

    @Override
    void onBindViewHolder(@NonNull List<RosteredShift> rosteredShifts, int position, @NonNull ItemViewHolder holder) {
        if (position == ROW_NUMBER_LOGGED_SHIFTS) {
            holder.setPrimaryIcon(R.drawable.ic_sigma_black_24dp);
            int loggedCount = 0;
            for (RosteredShift rosteredShift : rosteredShifts) {
                if (rosteredShift.getLoggedShiftData() != null) {
                    loggedCount++;
                }
            }
            holder.setText("Selected logged shifts", getCountString(loggedCount));
        } else if (position == ROW_NUMBER_TIME_PERIOD) {
            holder.setPrimaryIcon(R.drawable.ic_duration_black_24dp);
            ZonedDateTime start = null, end = null;
            for (RosteredShift rosteredShift : rosteredShifts) {
                if (rosteredShift.getLoggedShiftData() != null) {
                    if (start == null) {
                        start = rosteredShift.getLoggedShiftData().getStart();
                    }
                    end = rosteredShift.getLoggedShiftData().getEnd();
                }
            }
            final String secondLine, thirdLine;
            if (start == null) {
                secondLine = getContext().getString(R.string.not_applicable);
                thirdLine = null;
            } else {
                LocalDate startDate = start.toLocalDate(), endDate = end.toLocalDate();
                secondLine = DateTimeUtils.getDateSpanString(startDate, endDate);
                thirdLine = "Days: " + getCountString((int) Duration.between(start, end).toDays() + 1);
            }
            holder.setText("Time period", secondLine, thirdLine);
        } else if (position == ROW_NUMBER_ROSTERED_HOURS) {
            holder.setPrimaryIcon(R.drawable.compliant_black_24dp);
            int loggedCount = 0;
            Duration duration = Duration.ZERO;
            for (RosteredShift rosteredShift : rosteredShifts) {
                if (rosteredShift.getLoggedShiftData() != null) {
                    loggedCount++;
                    duration = duration.plus(rosteredShift.getShiftData().getDuration());
                }
            }
            final String secondLine;
            if (loggedCount == 0) {
                secondLine = getContext().getString(R.string.not_applicable);
            } else {
                secondLine = DateTimeUtils.getDurationString(getContext(), duration);
            }
            holder.setText("Rostered hours", secondLine);
        } else if (position == ROW_NUMBER_LOGGED_HOURS) {
            holder.setPrimaryIcon(R.drawable.ic_clipboard_black_24dp);
            int loggedCount = 0;
            Duration duration = Duration.ZERO;
            for (RosteredShift rosteredShift : rosteredShifts) {
                if (rosteredShift.getLoggedShiftData() != null) {
                    loggedCount++;
                    duration = duration.plus(rosteredShift.getLoggedShiftData().getDuration());
                }
            }
            final String secondLine;
            if (loggedCount == 0) {
                secondLine = getContext().getString(R.string.not_applicable);
            } else {
                secondLine = DateTimeUtils.getDurationString(getContext(), duration);
            }
            holder.setText("Logged hours", secondLine);
        } else if (position == ROW_NUMBER_DIFFERENCE_HOURS) {
            holder.setPrimaryIcon(R.drawable.ic_add_black_24dp);
            int loggedCount = 0;
            Duration rosteredDuration = Duration.ZERO, loggedDuration = Duration.ZERO;
            for (RosteredShift rosteredShift : rosteredShifts) {
                if (rosteredShift.getLoggedShiftData() != null) {
                    loggedCount++;
                    rosteredDuration = rosteredDuration.plus(rosteredShift.getShiftData().getDuration());
                    loggedDuration = loggedDuration.plus(rosteredShift.getLoggedShiftData().getDuration());
                }
            }
            final String secondLine;
            if (loggedCount == 0) {
                secondLine = getContext().getString(R.string.not_applicable);
            } else if (rosteredDuration.compareTo(loggedDuration) == 1) {
                secondLine = '-' + DateTimeUtils.getDurationString(getContext(), rosteredDuration.minus(loggedDuration));
            } else {
                secondLine = DateTimeUtils.getDurationString(getContext(), loggedDuration.minus(rosteredDuration));
            }
            holder.setText("Difference", secondLine);
        }
    }

}
