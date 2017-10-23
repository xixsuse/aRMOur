package com.skepticalone.armour.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Compliance;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

public final class RosteredShiftDetailAdapter extends ItemDetailAdapter<RosteredShift> {
    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_SHIFT_TYPE = 3,
            ROW_NUMBER_TOGGLE_LOGGED = 4,
            NUMBER_OF_ROWS_FOR_LOGGED = 2;
    @NonNull
    private final ShiftDetailAdapterHelper<RosteredShift> shiftDetailAdapterHelper;
    @NonNull
    private final Callbacks callbacks;
    private int
            rowNumberLoggedStart,
            rowNumberLoggedEnd,
            rowNumberComment,
            rowNumberDurationBetweenShifts,
            rowNumberDurationWorkedOverDay,
            rowNumberDurationWorkedOverWeek,
            rowNumberDurationWorkedOverFortnight,
            rowNumberLastWeekendWorked,
            rowNumberLongDays,
            rowNumberConsecutiveDays,
            rowNumberConsecutiveNights,
            rowNumberRecoveryAfterNights,
            rowCount;

    public RosteredShiftDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context, callbacks);
        this.callbacks = callbacks;
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper<RosteredShift>(callbacks) {
            
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

    @Override
    void onChanged(@Nullable RosteredShift oldRosteredShift, @Nullable RosteredShift rosteredShift) {
        if (oldRosteredShift == null && rosteredShift != null) {
            int lastPosition = ROW_NUMBER_TOGGLE_LOGGED;
            if (rosteredShift.getLoggedShiftData() == null) {
                rowNumberLoggedStart = rowNumberLoggedEnd = RecyclerView.NO_POSITION;
            } else {
                rowNumberLoggedStart = ++lastPosition;
                rowNumberLoggedEnd = ++lastPosition;
            }
            rowNumberComment = ++lastPosition;
            rowNumberDurationBetweenShifts = ++lastPosition;
            rowNumberDurationWorkedOverDay = ++lastPosition;
            rowNumberDurationWorkedOverWeek = ++lastPosition;
            rowNumberDurationWorkedOverFortnight = ++lastPosition;
            rowNumberLastWeekendWorked = rosteredShift.getCompliance().getCurrentWeekend() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberLongDays = rosteredShift.getCompliance().getIndexOfLongDay() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberConsecutiveDays = rosteredShift.getCompliance().getIndexOfDay() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberConsecutiveNights = rosteredShift.getCompliance().getIndexOfNightShift() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberRecoveryAfterNights = rosteredShift.getCompliance().getRecoveryInformation() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowCount = ++lastPosition;
        }
        super.onChanged(oldRosteredShift, rosteredShift);
    }

    @Override
    int getRowNumberComment() {
        return rowNumberComment;
    }

    @Override
    int getFixedRowCount() {
        return rowCount;
    }

    @Override
    void notifyUpdated(@NonNull RosteredShift oldShift, @NonNull RosteredShift newShift) {
        super.notifyUpdated(oldShift, newShift);
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
        if (oldShift.getLoggedShiftData() == null && newShift.getLoggedShiftData() != null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            int lastPosition = ROW_NUMBER_TOGGLE_LOGGED;
            rowNumberLoggedStart = ++lastPosition;
            rowNumberLoggedEnd = ++lastPosition;
            rowNumberComment = ++lastPosition;
            rowNumberDurationBetweenShifts = ++lastPosition;
            rowNumberDurationWorkedOverDay = ++lastPosition;
            rowNumberDurationWorkedOverWeek = ++lastPosition;
            rowNumberDurationWorkedOverFortnight = ++lastPosition;
            if (rowNumberLastWeekendWorked != RecyclerView.NO_POSITION)
                rowNumberLastWeekendWorked = ++lastPosition;
            if (rowNumberLongDays != RecyclerView.NO_POSITION)
                rowNumberLongDays = ++lastPosition;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberConsecutiveNights != RecyclerView.NO_POSITION)
                rowNumberConsecutiveNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemRangeInserted(rowNumberLoggedStart, NUMBER_OF_ROWS_FOR_LOGGED);
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() == null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            notifyItemRangeRemoved(rowNumberLoggedStart, NUMBER_OF_ROWS_FOR_LOGGED);
            rowNumberLoggedStart = RecyclerView.NO_POSITION;
            rowNumberLoggedEnd = RecyclerView.NO_POSITION;
            int lastPosition = ROW_NUMBER_TOGGLE_LOGGED;
            rowNumberComment = ++lastPosition;
            rowNumberDurationBetweenShifts = ++lastPosition;
            rowNumberDurationWorkedOverDay = ++lastPosition;
            rowNumberDurationWorkedOverWeek = ++lastPosition;
            rowNumberDurationWorkedOverFortnight = ++lastPosition;
            if (rowNumberLastWeekendWorked != RecyclerView.NO_POSITION)
                rowNumberLastWeekendWorked = ++lastPosition;
            if (rowNumberLongDays != RecyclerView.NO_POSITION)
                rowNumberLongDays = ++lastPosition;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberConsecutiveNights != RecyclerView.NO_POSITION)
                rowNumberConsecutiveNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() != null) {
            if (!oldShift.getLoggedShiftData().getDuration().equals(newShift.getLoggedShiftData().getDuration())) {
                notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            }
            if (!oldShift.getLoggedShiftData().getStart().toLocalTime().equals(newShift.getLoggedShiftData().getStart().toLocalTime())) {
                notifyItemChanged(rowNumberLoggedStart);
            }
            if (!oldShift.getLoggedShiftData().getEnd().toLocalTime().equals(newShift.getLoggedShiftData().getEnd().toLocalTime()) || (oldShift.getShiftData().getStart().toLocalDate().isEqual(oldShift.getLoggedShiftData().getEnd().toLocalDate()) ? !newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getLoggedShiftData().getEnd().toLocalDate()) : (newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getLoggedShiftData().getEnd().toLocalDate()) || !oldShift.getLoggedShiftData().getEnd().getDayOfWeek().equals(newShift.getLoggedShiftData().getEnd().getDayOfWeek())))) {
                notifyItemChanged(rowNumberLoggedEnd);
            }
        }
        if (!Comparators.equalCompliance(oldShift.getCompliance().sufficientDurationBetweenShifts(), newShift.getCompliance().sufficientDurationBetweenShifts()) || !(oldShift.getCompliance().getDurationBetweenShifts() == null ? newShift.getCompliance().getDurationBetweenShifts() == null : (newShift.getCompliance().getDurationBetweenShifts() != null && oldShift.getCompliance().getDurationBetweenShifts().equals(newShift.getCompliance().getDurationBetweenShifts())))) {
            notifyItemChanged(rowNumberDurationBetweenShifts);
        }
        if (!Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumDurationOverDay(), newShift.getCompliance().compliesWithMaximumDurationOverDay()) || !oldShift.getCompliance().getDurationOverDay().equals(newShift.getCompliance().getDurationOverDay())) {
            notifyItemChanged(rowNumberDurationWorkedOverDay);
        }
        if (!Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumDurationOverWeek(), newShift.getCompliance().compliesWithMaximumDurationOverWeek()) || !oldShift.getCompliance().getDurationOverWeek().equals(newShift.getCompliance().getDurationOverWeek())) {
            notifyItemChanged(rowNumberDurationWorkedOverWeek);
        }
        if (!Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumDurationOverFortnight(), newShift.getCompliance().compliesWithMaximumDurationOverFortnight()) || !oldShift.getCompliance().getDurationOverFortnight().equals(newShift.getCompliance().getDurationOverFortnight())) {
            notifyItemChanged(rowNumberDurationWorkedOverFortnight);
        }
        if (oldShift.getCompliance().getCurrentWeekend() == null && newShift.getCompliance().getCurrentWeekend() != null) {
            int lastPosition = rowNumberDurationWorkedOverFortnight;
            rowNumberLastWeekendWorked = ++lastPosition;
            if (rowNumberLongDays != RecyclerView.NO_POSITION)
                rowNumberLongDays = ++lastPosition;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberConsecutiveNights != RecyclerView.NO_POSITION)
                rowNumberConsecutiveNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberLastWeekendWorked);
        } else if (oldShift.getCompliance().getCurrentWeekend() != null && newShift.getCompliance().getCurrentWeekend() == null) {
            notifyItemRemoved(rowNumberLastWeekendWorked);
            rowNumberLastWeekendWorked = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberDurationWorkedOverFortnight;
            if (rowNumberLongDays != RecyclerView.NO_POSITION)
                rowNumberLongDays = ++lastPosition;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberConsecutiveNights != RecyclerView.NO_POSITION)
                rowNumberConsecutiveNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getCurrentWeekend() != null && newShift.getCompliance().getCurrentWeekend() != null && (
                !oldShift.getCompliance().getCurrentWeekend().isEqual(newShift.getCompliance().getCurrentWeekend()) ||
                        !Comparators.equalDates(oldShift.getCompliance().getLastWeekendWorked(), newShift.getCompliance().getLastWeekendWorked()) ||
                        !Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumWeekendsWorked(), newShift.getCompliance().compliesWithMaximumWeekendsWorked())
        )) {
            notifyItemChanged(rowNumberLastWeekendWorked);
        }
        if (oldShift.getCompliance().getIndexOfLongDay() == null && newShift.getCompliance().getIndexOfLongDay() != null) {
            int lastPosition = rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            rowNumberLongDays = ++lastPosition;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberConsecutiveNights != RecyclerView.NO_POSITION)
                rowNumberConsecutiveNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberLongDays);
        } else if (oldShift.getCompliance().getIndexOfLongDay() != null && newShift.getCompliance().getIndexOfLongDay() == null) {
            notifyItemRemoved(rowNumberLongDays);
            rowNumberLongDays = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberConsecutiveNights != RecyclerView.NO_POSITION)
                rowNumberConsecutiveNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getIndexOfLongDay() != null && newShift.getCompliance().getIndexOfLongDay() != null && (
                !oldShift.getCompliance().getIndexOfLongDay().equals(newShift.getCompliance().getIndexOfLongDay()) ||
                        !Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumLongDaysPerWeek(), newShift.getCompliance().compliesWithMaximumLongDaysPerWeek())
        )) {
            notifyItemChanged(rowNumberLongDays);
        }
        if (oldShift.getCompliance().getIndexOfDay() == null && newShift.getCompliance().getIndexOfDay() != null) {
            int lastPosition = rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberConsecutiveNights != RecyclerView.NO_POSITION)
                rowNumberConsecutiveNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberConsecutiveDays);
        } else if (oldShift.getCompliance().getIndexOfDay() != null && newShift.getCompliance().getIndexOfDay() == null) {
            notifyItemRemoved(rowNumberConsecutiveDays);
            rowNumberConsecutiveDays = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            if (rowNumberConsecutiveNights != RecyclerView.NO_POSITION)
                rowNumberConsecutiveNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getIndexOfDay() != null && newShift.getCompliance().getIndexOfDay() != null && (
                !oldShift.getCompliance().getIndexOfDay().equals(newShift.getCompliance().getIndexOfDay()) ||
                        !Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumDaysPerWeek(), newShift.getCompliance().compliesWithMaximumDaysPerWeek())
        )) {
            notifyItemChanged(rowNumberConsecutiveDays);
        }
        if (oldShift.getCompliance().getIndexOfNightShift() == null && newShift.getCompliance().getIndexOfNightShift() != null) {
            int lastPosition = rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            rowNumberConsecutiveNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberConsecutiveNights);
        } else if (oldShift.getCompliance().getIndexOfNightShift() != null && newShift.getCompliance().getIndexOfNightShift() == null) {
            notifyItemRemoved(rowNumberConsecutiveNights);
            rowNumberConsecutiveNights = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getIndexOfNightShift() != null && newShift.getCompliance().getIndexOfNightShift() != null && (
                !oldShift.getCompliance().getIndexOfNightShift().equals(newShift.getCompliance().getIndexOfNightShift()) ||
                        !Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumConsecutiveNightsWorked(), newShift.getCompliance().compliesWithMaximumConsecutiveNightsWorked())
        )) {
            notifyItemChanged(rowNumberConsecutiveNights);
        }
        if (oldShift.getCompliance().getRecoveryInformation() == null && newShift.getCompliance().getRecoveryInformation() != null) {
            int lastPosition = rowNumberConsecutiveNights != RecyclerView.NO_POSITION ? rowNumberConsecutiveNights : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberRecoveryAfterNights);
        } else if (oldShift.getCompliance().getRecoveryInformation() != null && newShift.getCompliance().getRecoveryInformation() == null) {
            notifyItemRemoved(rowNumberRecoveryAfterNights);
            rowNumberRecoveryAfterNights = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberConsecutiveNights != RecyclerView.NO_POSITION ? rowNumberConsecutiveNights : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getRecoveryInformation() != null && newShift.getCompliance().getRecoveryInformation() != null && (
                !oldShift.getCompliance().getRecoveryInformation().isEqualTo(newShift.getCompliance().getRecoveryInformation()) ||
                        !Comparators.equalCompliance(oldShift.getCompliance().sufficientRecoveryFollowingNights(), newShift.getCompliance().sufficientRecoveryFollowingNights())
        )) {
            notifyItemChanged(rowNumberRecoveryAfterNights);
        }
    }

    @Override
    void onBindViewHolder(@NonNull RosteredShift shift, int position, @NonNull ItemViewHolder holder) {
        if (position == ROW_NUMBER_TOGGLE_LOGGED) {
            holder.setupSwitch();
            holder.bindSwitch(shift.getLoggedShiftData() != null, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean logged) {
                    callbacks.setLogged(logged);
                }
            });
            holder.setPrimaryIcon(R.drawable.ic_clipboard_black_24dp);
            holder.hideSecondaryIcon();
            holder.setText(getContext().getString(R.string.logged), shift.getLoggedShiftData() == null ? null : DateTimeUtils.getDurationString(getContext(), shift.getLoggedShiftData().getDuration()));
        } else if (position == rowNumberLoggedStart) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_clipboard_play_black_24dp);
            holder.hideSecondaryIcon();
            //noinspection ConstantConditions
            holder.setText(getContext().getString(R.string.logged_start), DateTimeUtils.getTimeString(shift.getLoggedShiftData().getStart().toLocalTime()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(true, true);
                }
            });
        } else if (position == rowNumberLoggedEnd) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_clipboard_stop_black_24dp);
            holder.hideSecondaryIcon();
            //noinspection ConstantConditions
            holder.setText(getContext().getString(R.string.logged_end), DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getEnd().toLocalDateTime(), shift.getShiftData().getStart().toLocalDate()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(false, true);
                }
            });
        } else if (position == rowNumberDurationBetweenShifts) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_sleep_black_24dp);
            holder.setCompliant(shift.getCompliance().sufficientDurationBetweenShifts());
            holder.setText(getContext().getString(R.string.time_between_shifts), shift.getCompliance().getDurationBetweenShifts() == null ? getContext().getString(R.string.not_applicable) : DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationBetweenShifts()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_minimum_hours_between_shifts, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS));
                }
            });
        } else if (position == rowNumberDurationWorkedOverDay) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_duration_black_24dp);
            holder.setCompliant(shift.getCompliance().compliesWithMaximumDurationOverDay());
            holder.setText(getContext().getString(R.string.duration_worked_over_day), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverDay()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_day, AppConstants.MAXIMUM_HOURS_OVER_DAY));
                }
            });
        } else if (position == rowNumberDurationWorkedOverWeek) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_week_black_24dp);
            holder.setCompliant(shift.getCompliance().compliesWithMaximumDurationOverWeek());
            holder.setText(getContext().getString(R.string.duration_worked_over_week), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverWeek()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_week, AppConstants.MAXIMUM_HOURS_OVER_WEEK));
                }
            });
        } else if (position == rowNumberDurationWorkedOverFortnight) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_weeks_black_24dp);
            holder.setCompliant(shift.getCompliance().compliesWithMaximumDurationOverFortnight());
            holder.setText(getContext().getString(R.string.duration_worked_over_fortnight), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverFortnight()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_fortnight, AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT));
                }
            });
        } else if (position == rowNumberLastWeekendWorked) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_weekend_black_24dp);
            holder.setCompliant(shift.getCompliance().compliesWithMaximumWeekendsWorked());
            final String secondLine, thirdLine;
            if (shift.getCompliance().getLastWeekendWorked() == null) {
                secondLine = getContext().getString(R.string.not_applicable);
                thirdLine = null;
            } else {
                secondLine = DateTimeUtils.getWeekendDateSpanString(shift.getCompliance().getLastWeekendWorked());
                //noinspection ConstantConditions
                thirdLine = DateTimeUtils.getWeeksAgo(getContext(), shift.getCompliance().getCurrentWeekend(), shift.getCompliance().getLastWeekendWorked());
            }
            holder.setText(getContext().getString(R.string.last_weekend_worked), secondLine, thirdLine);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_consecutive_weekends));
                }
            });
        } else if (position == rowNumberLongDays) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_long_day_black_24dp);
            holder.setCompliant(shift.getCompliance().compliesWithMaximumLongDaysPerWeek());
            //noinspection ConstantConditions
            int longDays = shift.getCompliance().getIndexOfLongDay() + 1;
            holder.setText(getContext().getString(R.string.number_of_long_days_in_week), getContext().getResources().getQuantityString(R.plurals.long_days, longDays, longDays));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_long_days_over_week, AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK, AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY));
                }
            });
        } else if (position == rowNumberConsecutiveDays) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_consecutive_shifts_black_24dp);
            holder.setCompliant(shift.getCompliance().compliesWithMaximumDaysPerWeek());
            //noinspection ConstantConditions
            int days = shift.getCompliance().getIndexOfDay() + 1;
            holder.setText(getContext().getString(R.string.number_of_consecutive_days_worked), getContext().getResources().getQuantityString(R.plurals.days, days, days));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.showMessage(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(getContext().getString(R.string.key_safer_rosters), getContext().getResources().getBoolean(R.bool.default_safer_rosters)) ?
                            getContext().getString(R.string.meca_safer_rosters_maximum_consecutive_days, AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_DAYS) :
                            getContext().getString(R.string.meca_maximum_consecutive_days, AppConstants.MAXIMUM_CONSECUTIVE_DAYS)
                    );
                }
            });
        } else if (position == rowNumberConsecutiveNights) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_consecutive_shifts_black_24dp);
            holder.setCompliant(shift.getCompliance().compliesWithMaximumConsecutiveNightsWorked());
            //noinspection ConstantConditions
            int nights = shift.getCompliance().getIndexOfNightShift() + 1;
            holder.setText(getContext().getString(R.string.number_of_consecutive_nights_worked), getContext().getResources().getQuantityString(R.plurals.nights, nights, nights));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    callbacks.showMessage(sharedPreferences.getBoolean(getContext().getString(R.string.key_safer_rosters), getContext().getResources().getBoolean(R.bool.default_safer_rosters)) ?
                            getContext().getString(R.string.meca_safer_rosters_maximum_consecutive_nights, sharedPreferences.getBoolean(getContext().getString(R.string.key_allow_5_consecutive_nights), getContext().getResources().getBoolean(R.bool.default_allow_5_consecutive_nights)) ? AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_STRICT) :
                            getContext().getString(R.string.meca_safer_rosters_maximum_consecutive_nights, AppConstants.MAXIMUM_CONSECUTIVE_NIGHTS)
                    );
                }
            });
        } else if (position == rowNumberRecoveryAfterNights) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_recovery_days_following_nights);
            holder.setCompliant(shift.getCompliance().sufficientRecoveryFollowingNights());
            final Compliance.RecoveryInformation recoveryInformation = shift.getCompliance().getRecoveryInformation();
            //noinspection ConstantConditions
            holder.setText(getContext().getString(R.string.recovery_days_following_nights), getContext().getString(R.string.n_days_following_n_nights, getContext().getResources().getQuantityString(R.plurals.days, recoveryInformation.recoveryDaysFollowingNights, recoveryInformation.recoveryDaysFollowingNights), getContext().getResources().getQuantityString(R.plurals.nights, recoveryInformation.previousConsecutiveNightShifts, recoveryInformation.previousConsecutiveNightShifts)));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    if (sharedPreferences.getBoolean(getContext().getString(R.string.key_safer_rosters), getContext().getResources().getBoolean(R.bool.default_safer_rosters))) {
                        if (recoveryInformation.previousConsecutiveNightShifts < AppConstants.SAFER_ROSTERS_MINIMUM_NIGHTS_BEFORE_RECOVERY) {
                            callbacks.showMessage(getContext().getString(R.string.recovery_days_only_required_following_n_nights, getContext().getResources().getQuantityString(R.plurals.nights, AppConstants.SAFER_ROSTERS_MINIMUM_NIGHTS_BEFORE_RECOVERY, AppConstants.SAFER_ROSTERS_MINIMUM_NIGHTS_BEFORE_RECOVERY)));
                        } else if (recoveryInformation.previousConsecutiveNightShifts == 2) {
                            callbacks.showMessage(getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 2, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS));
                        } else if (recoveryInformation.previousConsecutiveNightShifts == 3) {
                            if (sharedPreferences.getBoolean(getContext().getString(R.string.key_allow_only_1_recovery_day_following_3_nights), getContext().getResources().getBoolean(R.bool.default_allow_only_1_recovery_day_following_3_nights))) {
                                callbacks.showMessage(getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT));
                            } else {
                                callbacks.showMessage(getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT));
                            }
                        } else {
                            callbacks.showMessage(getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 4, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS));
                        }
                    } else {
                        callbacks.showMessage(getContext().getString(R.string.meca_minimum_recovery_after_consecutive_nights, AppConstants.MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS, AppConstants.MINIMUM_NIGHTS_BEFORE_RECOVERY));
                    }
                }
            });
        } else {
            holder.hideSecondaryIcon();
            if (!shiftDetailAdapterHelper.bindViewHolder(shift, position, holder, this)) {
                super.onBindViewHolder(shift, position, holder);
            }
        }
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, DateDetailAdapterHelper.Callbacks {
        void setLogged(boolean logged);
        void changeTime(boolean start, boolean logged);
        void showMessage(@NonNull String message);
    }

}
