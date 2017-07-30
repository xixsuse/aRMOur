package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftDetailAdapter extends ItemDetailAdapter<RosteredShiftEntity> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_SHIFT_TYPE = 3,
            ROW_NUMBER_TOGGLE_LOGGED = 4,
            ROW_NUMBER_LOGGED_START = 5,
            ROW_NUMBER_LOGGED_END = 6,
            ROW_NUMBER_COMMENT_IF_LOGGED = 7,
            ROW_NUMBER_DURATION_BETWEEN_SHIFTS_IF_LOGGED = 8,
            ROW_NUMBER_DURATION_WORKED_OVER_DAY_IF_LOGGED = 9,
            ROW_NUMBER_DURATION_WORKED_OVER_WEEK_IF_LOGGED = 10,
            ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT_IF_LOGGED = 11,
            ROW_NUMBER_WEEKEND_IF_LOGGED = 12,
            ROW_COUNT_IF_LOGGED = 13,
            NUMBER_OF_ROWS_FOR_LOGGED = 2;

    @NonNull
    private final ShiftDetailAdapterHelper<RosteredShiftEntity> shiftDetailAdapterHelper;

    @NonNull
    private final Callbacks callbacks;
    
    public RosteredShiftDetailAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks);
        this.callbacks = callbacks;
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper<RosteredShiftEntity>(callbacks, calculator) {
            
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
            void changeTime(boolean start) {
                RosteredShiftDetailAdapter.this.callbacks.changeTime(start, false);
            }
            
        };
    }

    private static int adjustForLogged(int valueIfLogged, @NonNull RosteredShiftEntity shift) {
        return valueIfLogged - (shift.getLoggedShiftData() == null ? NUMBER_OF_ROWS_FOR_LOGGED : 0);
    }

    @Override
    int getRowNumberComment(@NonNull RosteredShiftEntity shift) {
        return adjustForLogged(ROW_NUMBER_COMMENT_IF_LOGGED, shift);
    }

    @Override
    int getRowCount(@NonNull RosteredShiftEntity shift) {
        return adjustForLogged(ROW_COUNT_IF_LOGGED, shift);
    }
    
    @Override
    void onItemUpdated(@NonNull RosteredShiftEntity oldShift, @NonNull RosteredShiftEntity newShift) {
        super.onItemUpdated(oldShift, newShift);
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
        if (oldShift.getDurationBetweenShifts() == null ? newShift.getDurationBetweenShifts() != null : (newShift.getDurationBetweenShifts() == null || !oldShift.getDurationBetweenShifts().isEqual(newShift.getDurationBetweenShifts()))) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_DURATION_BETWEEN_SHIFTS_IF_LOGGED, oldShift));
        }
        if (!oldShift.getDurationOverDay().isEqual(newShift.getDurationOverDay())) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_DAY_IF_LOGGED, oldShift));
        }
        if (!oldShift.getDurationOverWeek().isEqual(newShift.getDurationOverWeek())) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_WEEK_IF_LOGGED, oldShift));
        }
        if (!oldShift.getDurationOverFortnight().isEqual(newShift.getDurationOverFortnight())) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT_IF_LOGGED, oldShift));
        }
        if (oldShift.getCurrentWeekend() == null ? newShift.getCurrentWeekend() != null : (newShift.getCurrentWeekend() == null || !oldShift.getCurrentWeekend().isEqual(newShift.getCurrentWeekend()) || (oldShift.getLastWeekendWorked() == null ? newShift.getLastWeekendWorked() != null : (newShift.getLastWeekendWorked() == null || !oldShift.getLastWeekendWorked().isEqual(newShift.getLastWeekendWorked()))))) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_WEEKEND_IF_LOGGED, oldShift));
        }
        if (oldShift.getLoggedShiftData() == null && newShift.getLoggedShiftData() != null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            notifyItemRangeInserted(ROW_NUMBER_LOGGED_START, NUMBER_OF_ROWS_FOR_LOGGED);
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() == null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            notifyItemRangeRemoved(ROW_NUMBER_LOGGED_START, NUMBER_OF_ROWS_FOR_LOGGED);
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() != null) {
            boolean dateChanged = !Comparators.equalLocalDates(oldShift.getShiftData().getStart().toLocalDate(), newShift.getShiftData().getStart().toLocalDate());
            if (dateChanged || !oldShift.getLoggedShiftData().getStart().toLocalTime().isEqual(newShift.getLoggedShiftData().getStart().toLocalTime())) {
                notifyItemChanged(ROW_NUMBER_LOGGED_START);
            }
            if (dateChanged || !oldShift.getLoggedShiftData().getEnd().toLocalTime().isEqual(newShift.getLoggedShiftData().getEnd().toLocalTime())) {
                notifyItemChanged(ROW_NUMBER_LOGGED_END);
            }
            if (!oldShift.getLoggedShiftData().getDuration().isEqual(newShift.getLoggedShiftData().getDuration())) {
                notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            }
        }
    }

    @Override
    boolean bindViewHolder(@NonNull RosteredShiftEntity shift, ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_TOGGLE_LOGGED) {
            final boolean switchChecked;
            final String secondLine;
            if (shift.getLoggedShiftData() == null) {
                switchChecked = false;
                secondLine = null;
            } else {
                switchChecked = true;
                secondLine = DateTimeUtils.getPeriodString(shift.getLoggedShiftData().getDuration());
            }
            holder.setupSwitch(R.drawable.ic_clipboard_black_24dp, switchChecked, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean logged) {
                    callbacks.setLogged(logged);
                }
            });
            holder.setText(holder.getText(R.string.logged), secondLine);
            holder.secondaryIcon.setVisibility(View.GONE);
            return true;
        } else if (position == ROW_NUMBER_LOGGED_START && shift.getLoggedShiftData() != null) {
            holder.setupPlain(R.drawable.ic_clipboard_play_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(true, true);
                }
            });
            holder.setText(holder.getText(R.string.logged_start), DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getStart(), shift.getShiftData().getStart().toLocalDate()));
            holder.secondaryIcon.setVisibility(View.GONE);
            return true;
        } else if (position == ROW_NUMBER_LOGGED_END && shift.getLoggedShiftData() != null) {
            holder.setupPlain(R.drawable.ic_clipboard_stop_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(false, true);
                }
            });
            holder.setText(holder.getText(R.string.logged_end), DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getEnd(), shift.getShiftData().getStart().toLocalDate()));
            holder.secondaryIcon.setVisibility(View.GONE);
            return true;
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_BETWEEN_SHIFTS_IF_LOGGED, shift)) {
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
                holder.setCompliant(!shift.insufficientDurationBetweenShifts());
            }
            return true;
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_DAY_IF_LOGGED, shift)) {
            holder.setupPlain(R.drawable.ic_duration_black_24dp, null);
            holder.setText(
                    holder.getText(R.string.duration_worked_over_day),
                    DateTimeUtils.getPeriodString(shift.getDurationOverDay())
            );
            holder.setCompliant(!shift.exceedsMaximumDurationOverDay());
            return true;
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_WEEK_IF_LOGGED, shift)) {
            holder.setupPlain(R.drawable.ic_week_black_24dp, null);
            holder.setText(
                    holder.getText(R.string.duration_worked_over_week),
                    DateTimeUtils.getPeriodString(shift.getDurationOverWeek())
            );
            holder.setCompliant(!shift.exceedsMaximumDurationOverWeek());
            return true;
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT_IF_LOGGED, shift)) {
            holder.setupPlain(R.drawable.ic_fortnight_black_24dp, null);
            holder.setText(
                    holder.getText(R.string.duration_worked_over_fortnight),
                    DateTimeUtils.getPeriodString(shift.getDurationOverFortnight())
            );
            holder.setCompliant(!shift.exceedsMaximumDurationOverFortnight());
            return true;
        } else if (position == adjustForLogged(ROW_NUMBER_WEEKEND_IF_LOGGED, shift)) {
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
                holder.setCompliant(!shift.consecutiveWeekendsWorked());
            }
            return true;
        } else {
            holder.secondaryIcon.setVisibility(View.GONE);
            return shiftDetailAdapterHelper.bindViewHolder(shift, holder, position) ||
                    super.bindViewHolder(shift, holder, position);
        }
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, DateDetailAdapterHelper.Callbacks {
        void setLogged(boolean logged);
        void changeTime(boolean start, boolean logged);
    }

}
