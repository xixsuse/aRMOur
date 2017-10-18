package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZonedDateTime;

import java.math.BigDecimal;
import java.util.List;

public final class RunReviewAdapter extends ItemTotalsAdapter<RosteredShift> {

    private static final int
            ROW_NUMBER_LOGGED_SHIFTS = 0,
            ROW_NUMBER_TIME_PERIOD = 1,
            ROW_NUMBER_LOGGED_DURATION = 2,
            ROW_NUMBER_RUN_CATEGORY = 3,
            ROW_NUMBER_ROSTERED_DURATION = 4,
            ROW_NUMBER_DIFFERENCE = 5,
            ROW_COUNT = 6;

    private int logged, notLogged, weeks;
    private ZonedDateTime start, end;
    private Duration loggedDuration, rosteredDuration;
    private BigDecimal hoursPerWeek;

    public RunReviewAdapter(@NonNull Context context) {
        super(context, R.string.run_review);
    }

    @Override
    int getFixedRowCount() {
        return ROW_COUNT;
    }

    @Override
    public void onChanged(@Nullable List<RosteredShift> rosteredShifts) {
        if (rosteredShifts != null) {
            logged = 0;
            start = end = null;
            loggedDuration = rosteredDuration = Duration.ZERO;
            for (RosteredShift rosteredShift : rosteredShifts) {
                if (rosteredShift.getLoggedShiftData() != null) {
                    if (start == null) {
                        start = rosteredShift.getLoggedShiftData().getStart();
                    }
                    loggedDuration = loggedDuration.plus(rosteredShift.getLoggedShiftData().getDuration());
                    rosteredDuration = rosteredDuration.plus(rosteredShift.getShiftData().getDuration());
                    end = rosteredShift.getLoggedShiftData().getEnd();
                    logged++;
                }
            }
            notLogged = rosteredShifts.size() - logged;
            if (start != null) {
                weeks = 1;
                while (end.isAfter(start.plusWeeks(weeks))) {
                    weeks++;
                }
                hoursPerWeek = BigDecimal.valueOf(loggedDuration.getSeconds()).divide(BigDecimal.valueOf(3600L * weeks), 1, BigDecimal.ROUND_DOWN);
            }
        }
        super.onChanged(rosteredShifts);
    }

    @Override
    void onBindViewHolder(@NonNull List<RosteredShift> rosteredShifts, int position, @NonNull ItemViewHolder holder) {
        if (position == ROW_NUMBER_LOGGED_SHIFTS) {
            holder.setPrimaryIcon(R.drawable.ic_sigma_black_24dp);
            holder.setText(getContext().getResources().getQuantityString(R.plurals.logged_shifts, logged, logged), notLogged == 0 ? null : getContext().getResources().getQuantityString(R.plurals.shifts_not_logged, notLogged, notLogged));
        } else if (position == ROW_NUMBER_TIME_PERIOD) {
            holder.setPrimaryIcon(R.drawable.ic_weeks_black_24dp);
            final String secondLine, thirdLine;
            if (start == null) {
                secondLine = getContext().getString(R.string.not_applicable);
                thirdLine = null;
            } else {
                LocalDate startDate = start.toLocalDate(), endDate = end.toLocalDate();
                secondLine = DateTimeUtils.getDateSpanString(startDate, endDate);
                int days = (int) (endDate.toEpochDay() - startDate.toEpochDay() + 1);
                thirdLine = getContext().getResources().getQuantityString(R.plurals.days, days, days);
            }
            holder.setText(getContext().getString(R.string.run_review_period), secondLine, thirdLine);
        } else if (position == ROW_NUMBER_LOGGED_DURATION) {
            holder.setPrimaryIcon(R.drawable.ic_clipboard_black_24dp);
            final String secondLine, thirdLine;
            if (logged == 0) {
                secondLine = getContext().getString(R.string.not_applicable);
                thirdLine = null;
            } else {
                secondLine = DateTimeUtils.getDurationString(getContext(), loggedDuration);
                thirdLine = getContext().getString(R.string.hours_per_week_over_period_format, hoursPerWeek, getContext().getResources().getQuantityString(R.plurals.weeks, weeks, weeks));
            }
            holder.setText(getContext().getString(R.string.logged_duration), secondLine, thirdLine);
        } else if (position == ROW_NUMBER_RUN_CATEGORY) {
            holder.setPrimaryIcon(R.drawable.ic_linear_scale_black_24dp);
            String secondLine, thirdLine;
            if (logged == 0) {
                secondLine = getContext().getString(R.string.not_applicable);
                thirdLine = null;
            } else if (hoursPerWeek.intValue() < AppConstants.CATEGORY_F_HOURS) {
                secondLine = getContext().getString(R.string.not_applicable);
                thirdLine = getContext().getString(R.string.insufficient_hours_per_week);
            } else {
                final char category;
                final int min, max;
                if (hoursPerWeek.intValue() >= AppConstants.CATEGORY_A_HOURS) {
                    category = 'A';
                    min = AppConstants.CATEGORY_A_HOURS;
                    max = 0;
                } else if (hoursPerWeek.intValue() >= AppConstants.CATEGORY_B_HOURS) {
                    category = 'B';
                    min = AppConstants.CATEGORY_B_HOURS;
                    max = AppConstants.CATEGORY_A_HOURS;
                } else if (hoursPerWeek.intValue() >= AppConstants.CATEGORY_C_HOURS) {
                    category = 'C';
                    min = AppConstants.CATEGORY_C_HOURS;
                    max = AppConstants.CATEGORY_B_HOURS;
                } else if (hoursPerWeek.intValue() >= AppConstants.CATEGORY_D_HOURS) {
                    category = 'D';
                    min = AppConstants.CATEGORY_D_HOURS;
                    max = AppConstants.CATEGORY_C_HOURS;
                } else if (hoursPerWeek.intValue() >= AppConstants.CATEGORY_E_HOURS) {
                    category = 'E';
                    min = AppConstants.CATEGORY_E_HOURS;
                    max = AppConstants.CATEGORY_D_HOURS;
                } else {
                    category = 'F';
                    min = AppConstants.CATEGORY_F_HOURS;
                    max = AppConstants.CATEGORY_E_HOURS;
                }
                secondLine = getContext().getString(R.string.x_category, category);
                if (max == 0) {
                    thirdLine = getContext().getString(R.string.highest_category_hours_per_week_format, min);
                } else {
                    thirdLine = getContext().getString(R.string.range_hours_per_week_format, min, BigDecimal.valueOf(max).subtract(BigDecimal.valueOf(1, 1)));
                }
            }
            holder.setText(getContext().getString(R.string.run_category), secondLine, thirdLine);
        } else if (position == ROW_NUMBER_ROSTERED_DURATION) {
            holder.setPrimaryIcon(R.drawable.ic_duration_black_24dp);
            final String secondLine;
            if (logged == 0) {
                secondLine = getContext().getString(R.string.not_applicable);
            } else {
                secondLine = DateTimeUtils.getDurationString(getContext(), rosteredDuration);
            }
            holder.setText(getContext().getString(R.string.rostered_duration), secondLine);
        } else if (position == ROW_NUMBER_DIFFERENCE) {
            holder.setPrimaryIcon(R.drawable.ic_add_circle_black_24dp);
            final String secondLine;
            if (logged == 0) {
                secondLine = getContext().getString(R.string.not_applicable);
            } else if (rosteredDuration.compareTo(loggedDuration) == 1) {
                secondLine = '-' + DateTimeUtils.getDurationString(getContext(), rosteredDuration.minus(loggedDuration));
            } else {
                secondLine = DateTimeUtils.getDurationString(getContext(), loggedDuration.minus(rosteredDuration));
            }
            holder.setText(getContext().getString(R.string.unrostered_duration), secondLine);
        }
    }

}
