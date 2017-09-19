package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.entity.RosteredShiftEntity;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

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
            ROW_NUMBER_PERIOD_BETWEEN_SHIFTS_IF_LOGGED = 8,
            ROW_NUMBER_DURATION_WORKED_OVER_DAY_IF_LOGGED = 9,
            ROW_NUMBER_DURATION_WORKED_OVER_WEEK_IF_LOGGED = 10,
            ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT_IF_LOGGED = 11,
            ROW_NUMBER_LAST_WEEKEND_WORKED_IF_LOGGED = 12,
            ROW_COUNT_IF_LOGGED = 13,
            NUMBER_OF_ROWS_FOR_LOGGED = 2;

    @NonNull
    private final ShiftDetailAdapterHelper<RosteredShiftEntity> shiftDetailAdapterHelper;

    @NonNull
    private final Callbacks callbacks;

    public RosteredShiftDetailAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
        this.callbacks = callbacks;
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper<RosteredShiftEntity>(callbacks) {
            
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
        return adjustForLogged(ROW_COUNT_IF_LOGGED, shift) - (shift.getCurrentWeekend() == null ? 1 : 0);
    }
    
    @Override
    void onItemUpdated(@NonNull RosteredShiftEntity oldShift, @NonNull RosteredShiftEntity newShift) {
        super.onItemUpdated(oldShift, newShift);
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
        if (oldShift.getDurationBetweenShifts() == null ? newShift.getDurationBetweenShifts() != null : (newShift.getDurationBetweenShifts() == null || !oldShift.getDurationBetweenShifts().equals(newShift.getDurationBetweenShifts()))) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_PERIOD_BETWEEN_SHIFTS_IF_LOGGED, oldShift));
        }
        if (!oldShift.getDurationOverDay().equals(newShift.getDurationOverDay())) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_DAY_IF_LOGGED, oldShift));
        }
        if (!oldShift.getDurationOverWeek().equals(newShift.getDurationOverWeek())) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_WEEK_IF_LOGGED, oldShift));
        }
        if (!oldShift.getDurationOverFortnight().equals(newShift.getDurationOverFortnight())) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT_IF_LOGGED, oldShift));
        }
        if (oldShift.getCurrentWeekend() == null ? newShift.getCurrentWeekend() != null : (newShift.getCurrentWeekend() == null || !oldShift.getCurrentWeekend().isEqual(newShift.getCurrentWeekend()) || (oldShift.getLastWeekendWorked() == null ? newShift.getLastWeekendWorked() != null : (newShift.getLastWeekendWorked() == null || !oldShift.getLastWeekendWorked().isEqual(newShift.getLastWeekendWorked()))))) {
            notifyItemChanged(adjustForLogged(ROW_NUMBER_LAST_WEEKEND_WORKED_IF_LOGGED, oldShift));
        }
        if (oldShift.getLoggedShiftData() == null && newShift.getLoggedShiftData() != null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            notifyItemRangeInserted(ROW_NUMBER_LOGGED_START, NUMBER_OF_ROWS_FOR_LOGGED);
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() == null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            notifyItemRangeRemoved(ROW_NUMBER_LOGGED_START, NUMBER_OF_ROWS_FOR_LOGGED);
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() != null) {
            final ZoneId zoneId = ZoneId.systemDefault();
            final boolean dateChanged = !Comparators.equalLocalDates(oldShift.getShiftData().getStart().atZone(zoneId).toLocalDate(), newShift.getShiftData().getStart().atZone(zoneId).toLocalDate());
            final ZonedDateTime
                    oldLoggedStart = oldShift.getLoggedShiftData().getStart().atZone(zoneId),
                    oldLoggedEnd = oldShift.getLoggedShiftData().getEnd().atZone(zoneId),
                    newLoggedStart = newShift.getLoggedShiftData().getStart().atZone(zoneId),
                    newLoggedEnd = newShift.getLoggedShiftData().getEnd().atZone(zoneId);
            if (dateChanged || !oldLoggedStart.toLocalTime().equals(newLoggedStart.toLocalTime())) {
                notifyItemChanged(ROW_NUMBER_LOGGED_START);
            }
            if (dateChanged || !oldLoggedEnd.toLocalTime().equals(newLoggedEnd.toLocalTime())) {
                notifyItemChanged(ROW_NUMBER_LOGGED_END);
            }
            if (!Duration.between(oldShift.getLoggedShiftData().getStart(), oldShift.getLoggedShiftData().getEnd()).equals(Duration.between(newShift.getLoggedShiftData().getStart(), newShift.getLoggedShiftData().getEnd()))) {
                notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            }
        }
    }

    @Override
    boolean bindViewHolder(@NonNull RosteredShiftEntity shift, ItemViewHolder holder, int position) {
        final ZoneId zoneId = ZoneId.systemDefault();
        if (position == ROW_NUMBER_TOGGLE_LOGGED) {
            final boolean switchChecked;
            final String secondLine;
            if (shift.getLoggedShiftData() == null) {
                switchChecked = false;
                secondLine = null;
            } else {
                switchChecked = true;
                secondLine = DateTimeUtils.getDurationString(Duration.between(shift.getLoggedShiftData().getStart(), shift.getLoggedShiftData().getEnd()));
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
            holder.setText(holder.getText(R.string.logged_start), DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getStart().atZone(zoneId).toLocalDateTime(), shift.getShiftData().getStart().atZone(zoneId).toLocalDate()));
            holder.secondaryIcon.setVisibility(View.GONE);
            return true;
        } else if (position == ROW_NUMBER_LOGGED_END && shift.getLoggedShiftData() != null) {
            holder.setupPlain(R.drawable.ic_clipboard_stop_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(false, true);
                }
            });
            holder.setText(holder.getText(R.string.logged_end), DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getEnd().atZone(zoneId).toLocalDateTime(), shift.getShiftData().getStart().atZone(zoneId).toLocalDate()));
            holder.secondaryIcon.setVisibility(View.GONE);
            return true;
        } else if (position == adjustForLogged(ROW_NUMBER_PERIOD_BETWEEN_SHIFTS_IF_LOGGED, shift)) {
            holder.setupPlain(R.drawable.ic_sleep_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(view.getContext().getString(R.string.meca_minimum_hours_between_shifts, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS));
                }
            });
            if (shift.getDurationBetweenShifts() == null) {
                holder.setText(
                        holder.getText(R.string.time_between_shifts),
                        holder.getText(R.string.not_applicable)
                );
                holder.secondaryIcon.setVisibility(View.GONE);
            } else {
                holder.setText(
                        holder.getText(R.string.time_between_shifts),
                        DateTimeUtils.getDurationString(shift.getDurationBetweenShifts())
                );
                holder.setCompliant(R.string.key_check_duration_between_shifts, R.bool.default_check_duration_between_shifts, !shift.insufficientDurationBetweenShifts());
            }
            return true;
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_DAY_IF_LOGGED, shift)) {
            holder.setupPlain(R.drawable.ic_duration_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(view.getContext().getString(R.string.meca_maximum_hours_over_day, AppConstants.MAXIMUM_HOURS_OVER_DAY));
                }
            });
            holder.setText(
                    holder.getText(R.string.duration_worked_over_day),
                    DateTimeUtils.getDurationString(shift.getDurationOverDay())
            );
            holder.setCompliant(R.string.key_check_duration_over_day, R.bool.default_check_duration_over_day, !shift.exceedsMaximumDurationOverDay());
            return true;
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_WEEK_IF_LOGGED, shift)) {
            holder.setupPlain(R.drawable.ic_week_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(view.getContext().getString(R.string.meca_maximum_hours_over_week, AppConstants.MAXIMUM_HOURS_OVER_WEEK));
                }
            });
            holder.setText(
                    holder.getText(R.string.duration_worked_over_week),
                    DateTimeUtils.getDurationString(shift.getDurationOverWeek())
            );
            holder.setCompliant(R.string.key_check_duration_over_week, R.bool.default_check_duration_over_week, !shift.exceedsMaximumDurationOverWeek());
            return true;
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT_IF_LOGGED, shift)) {
            holder.setupPlain(R.drawable.ic_fortnight_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(view.getContext().getString(R.string.meca_maximum_hours_over_fortnight, AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT));
                }
            });
            holder.setText(
                    holder.getText(R.string.duration_worked_over_fortnight),
                    DateTimeUtils.getDurationString(shift.getDurationOverFortnight())
            );
            holder.setCompliant(R.string.key_check_duration_over_fortnight, R.bool.default_check_duration_over_fortnight, !shift.exceedsMaximumDurationOverFortnight());
            return true;
        } else if (shift.getCurrentWeekend() != null && position == adjustForLogged(ROW_NUMBER_LAST_WEEKEND_WORKED_IF_LOGGED, shift)) {
            holder.setupPlain(R.drawable.ic_weekend_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(view.getContext().getString(R.string.meca_consecutive_weekends));
                }
            });
            final String secondLine, thirdLine;
            if (shift.getLastWeekendWorked() == null) {
                secondLine = holder.getText(R.string.not_applicable);
                thirdLine = null;
            } else {
                secondLine = DateTimeUtils.getWeekendDateSpanString(shift.getLastWeekendWorked());
                thirdLine = DateTimeUtils.getWeeksAgo(shift.getLastWeekendWorked(), shift.getCurrentWeekend());
            }
            holder.setText(holder.getText(R.string.last_weekend_worked), secondLine, thirdLine);
            holder.setCompliant(R.string.key_check_consecutive_weekends, R.bool.default_check_consecutive_weekends, !shift.consecutiveWeekendsWorked());
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
        void showMessage(@NonNull String message);
    }

}
