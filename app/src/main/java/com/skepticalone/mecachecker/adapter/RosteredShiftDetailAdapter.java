package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.RosteredShift;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftDetailAdapter extends ItemDetailAdapter<RosteredShift> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_SHIFT_TYPE = 3,
            ROW_NUMBER_LOGGED_START = 4,
            ROW_NUMBER_LOGGED_END = 5,
            ROW_NUMBER_COMMENT = 6,
            ROW_NUMBER_INTERVAL_BETWEEN_SHIFTS = 7,
            ROW_NUMBER_DURATION_WORKED_OVER_DAY = 8,
            ROW_NUMBER_DURATION_WORKED_OVER_WEEK = 9,
            ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT = 10,
            ROW_NUMBER_LAST_WEEKEND_WORKED = 11,
            ROW_COUNT = 12;

    private final ShiftDetailAdapterHelper<RosteredShift> shiftDetailAdapterHelper;

    public RosteredShiftDetailAdapter(final Callbacks callbacks, ShiftUtil.Calculator calculator) {
        super(callbacks);
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper<RosteredShift>(calculator){
            @Override
            int getRowNumberDate() {
                return ROW_NUMBER_DATE;
            }

            @Override
            int getRowNumberStart() {
                return ROW_NUMBER_START;
            }

            @Override
            int getRowNumberEnd() {
                return ROW_NUMBER_END;
            }

            @Override
            int getRowNumberShiftType() {
                return ROW_NUMBER_SHIFT_TYPE;
            }

            @Override
            void changeDate(@NonNull RosteredShift shift) {
                callbacks.changeDate(shift.getId(), shift.getShiftData(), shift.getLoggedShiftData());
            }

            @Override
            void changeTime(@NonNull RosteredShift shift, boolean isStart) {
                callbacks.changeTime(shift.getId(), isStart, shift.getShiftData(), shift.getLoggedShiftData());
            }
        };
    }

    @Override
    int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getRowCount(@NonNull RosteredShift item) {
        return ROW_COUNT;
    }

    @Override
    void onItemUpdated(@NonNull RosteredShift oldShift, @NonNull RosteredShift newShift) {
        super.onItemUpdated(oldShift, newShift);
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
    }

    @Override
    boolean bindViewHolder(@NonNull RosteredShift shift, ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_LOGGED_START) {
            holder.setupPlain(R.drawable.ic_clipboard_play_black_24dp, null);
            holder.setText(holder.getText(R.string.logged_start), shift.getLoggedShiftData() == null ? holder.getText(R.string.not_applicable) :  DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getStart(), shift.getShiftData().getStart().toLocalDate()));
            holder.secondaryIcon.setVisibility(View.GONE);
            return true;
        } else if (position == ROW_NUMBER_LOGGED_END) {
            holder.setupPlain(R.drawable.ic_clipboard_stop_black_24dp, null);
            holder.setText(holder.getText(R.string.logged_end), shift.getLoggedShiftData() == null ? holder.getText(R.string.not_applicable) : DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getEnd(), shift.getShiftData().getStart().toLocalDate()));
            holder.secondaryIcon.setVisibility(View.GONE);
            return true;
        } else if (position == ROW_NUMBER_INTERVAL_BETWEEN_SHIFTS) {
            holder.setupPlain(R.drawable.ic_sleep_black_24dp, null);
            if (shift.getDurationBetweenShifts() == null) {
                holder.setText(
                        holder.getText(R.string.time_between_shifts),
                        holder.getText(R.string.not_applicable)
                );
                holder.secondaryIcon.setVisibility(View.GONE);
            } else {
                holder.setText(
                        holder.getText(R.string.time_between_shifts),
                        DateTimeUtils.getPeriodString(shift.getDurationBetweenShifts())
                );
                holder.secondaryIcon.setImageResource(shift.insufficientDurationBetweenShifts() ? R.drawable.ic_cancel_red_24dp : R.drawable.ic_check_black_24dp);
                holder.secondaryIcon.setVisibility(View.VISIBLE);
            }
            return true;
        } else if (position == ROW_NUMBER_DURATION_WORKED_OVER_DAY) {
            holder.setupPlain(R.drawable.ic_duration_black_24dp, null);
            holder.setText(
                    holder.getText(R.string.duration_worked_over_day),
                    DateTimeUtils.getPeriodString(shift.getDurationOverDay())
            );
            holder.secondaryIcon.setImageResource(shift.exceedsMaximumDurationOverDay() ? R.drawable.ic_cancel_red_24dp : R.drawable.ic_check_black_24dp);
            holder.secondaryIcon.setVisibility(View.VISIBLE);
            return true;
        } else if (position == ROW_NUMBER_DURATION_WORKED_OVER_WEEK) {
            holder.setupPlain(R.drawable.ic_week_black_24dp, null);
            holder.setText(
                    holder.getText(R.string.duration_worked_over_week),
                    DateTimeUtils.getPeriodString(shift.getDurationOverWeek())
            );
            holder.secondaryIcon.setImageResource(shift.exceedsMaximumDurationOverWeek() ? R.drawable.ic_cancel_red_24dp : R.drawable.ic_check_black_24dp);
            holder.secondaryIcon.setVisibility(View.VISIBLE);
            return true;
        } else if (position == ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT) {
            holder.setupPlain(R.drawable.ic_fortnight_black_24dp, null);
            holder.setText(
                    holder.getText(R.string.duration_worked_over_fortnight),
                    DateTimeUtils.getPeriodString(shift.getDurationOverFortnight())
            );
            holder.secondaryIcon.setImageResource(shift.exceedsMaximumDurationOverFortnight() ? R.drawable.ic_cancel_red_24dp : R.drawable.ic_check_black_24dp);
            holder.secondaryIcon.setVisibility(View.VISIBLE);
            return true;
        } else if (position == ROW_NUMBER_LAST_WEEKEND_WORKED) {
            holder.setupPlain(R.drawable.ic_weekend_black_24dp, null);
            if (shift.getCurrentWeekend() == null) {
                holder.setText(
                        holder.getText(R.string.current_weekend),
                        holder.getText(R.string.not_applicable)
                );
                holder.secondaryIcon.setVisibility(View.GONE);
            } else {
                holder.setText(
                        holder.getText(R.string.current_weekend),
                        DateTimeUtils.getWeekendDateSpanString(shift.getCurrentWeekend()),
                        holder.getText(R.string.last_weekend_worked) + ":\n" +
                                (shift.getLastWeekendWorked() == null ? holder.getText(R.string.not_applicable) : DateTimeUtils.getWeekendDateSpanString(shift.getLastWeekendWorked()))
                );
                holder.secondaryIcon.setImageResource(shift.consecutiveWeekendsWorked() ? R.drawable.ic_cancel_red_24dp : R.drawable.ic_check_black_24dp);
                holder.secondaryIcon.setVisibility(View.VISIBLE);
            }
            return true;
        } else {
            holder.secondaryIcon.setVisibility(View.GONE);
            return shiftDetailAdapterHelper.bindViewHolder(shift, holder, position) ||
                    super.bindViewHolder(shift, holder, position);
        }
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks {
        void changeDate(long id, @NonNull ShiftData shiftData, @Nullable ShiftData loggedShiftData);
        void changeTime(long id, boolean isStart, @NonNull ShiftData shiftData, @Nullable ShiftData loggedShiftData);
    }

}
