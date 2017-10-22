package com.skepticalone.armour.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.armour.BuildConfig;
import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Compliance;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

public final class RosteredShiftDetailAdapter extends ItemDetailAdapter<RosteredShift> {
    private static final String TAG = "RosteredDetailAdapter";
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
    private boolean initialised = false;
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
            rowNumberNights,
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
    public void onChanged(@Nullable RosteredShift rosteredShift) {
        if (!initialised && rosteredShift != null) {
            initialised = true;
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
            rowNumberNights = rosteredShift.getCompliance().getIndexOfNightShift() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberRecoveryAfterNights = rosteredShift.getCompliance().getRecoveryInformation() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowCount = ++lastPosition;
        }
        super.onChanged(rosteredShift);
    }

    @Override
    int getRowNumberComment() {
        return rowNumberComment;
    }

    @Override
    int getRowCount(@NonNull RosteredShift data) {
        return rowCount;
    }

    private void logAllRowNumbers() {
        Log.d(TAG, "logAllRowNumbers() called");
        Log.d(TAG, "logAllRowNumbers: rowNumberLoggedStart: " + rowNumberLoggedStart);
        Log.d(TAG, "logAllRowNumbers: rowNumberLoggedEnd: " + rowNumberLoggedEnd);
        Log.d(TAG, "logAllRowNumbers: rowNumberComment: " + rowNumberComment);
        Log.d(TAG, "logAllRowNumbers: rowNumberDurationBetweenShifts: " + rowNumberDurationBetweenShifts);
        Log.d(TAG, "logAllRowNumbers: rowNumberDurationWorkedOverDay: " + rowNumberDurationWorkedOverDay);
        Log.d(TAG, "logAllRowNumbers: rowNumberDurationWorkedOverWeek: " + rowNumberDurationWorkedOverWeek);
        Log.d(TAG, "logAllRowNumbers: rowNumberDurationWorkedOverFortnight: " + rowNumberDurationWorkedOverFortnight);
        Log.d(TAG, "logAllRowNumbers: rowNumberLastWeekendWorked: " + rowNumberLastWeekendWorked);
        Log.d(TAG, "logAllRowNumbers: rowNumberLongDays: " + rowNumberLongDays);
        Log.d(TAG, "logAllRowNumbers: rowNumberNights: " + rowNumberNights);
        Log.d(TAG, "logAllRowNumbers: rowNumberRecoveryAfterNights: " + rowNumberRecoveryAfterNights);
        Log.d(TAG, "logAllRowNumbers: rowCount: " + rowCount);
    }

    @Override
    void notifyUpdated(@NonNull RosteredShift oldShift, @NonNull RosteredShift newShift) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "notifyUpdated() called with: oldShift = [" + oldShift.getShiftData() + "], newShift = [" + newShift.getShiftData() + "]");
            logAllRowNumbers();
        }
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
            if (rowNumberNights != RecyclerView.NO_POSITION)
                rowNumberNights = ++lastPosition;
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
            if (rowNumberNights != RecyclerView.NO_POSITION)
                rowNumberNights = ++lastPosition;
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
            if (rowNumberNights != RecyclerView.NO_POSITION)
                rowNumberNights = ++lastPosition;
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
            if (rowNumberNights != RecyclerView.NO_POSITION)
                rowNumberNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getCurrentWeekend() != null && newShift.getCompliance().getCurrentWeekend() != null && (
                !oldShift.getCompliance().getCurrentWeekend().isEqual(newShift.getCompliance().getCurrentWeekend()) ||
                        !Comparators.equalDates(oldShift.getCompliance().getLastWeekendWorked(), newShift.getCompliance().getLastWeekendWorked()) ||
                        !Comparators.equalCompliance(oldShift.getCompliance().previousWeekendFree(), newShift.getCompliance().previousWeekendFree())
        )) {
            notifyItemChanged(rowNumberLastWeekendWorked);
        }
        if (oldShift.getCompliance().getIndexOfLongDay() == null && newShift.getCompliance().getIndexOfLongDay() != null) {
            int lastPosition = rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            rowNumberLongDays = ++lastPosition;
            if (rowNumberNights != RecyclerView.NO_POSITION)
                rowNumberNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberLongDays);
        } else if (oldShift.getCompliance().getIndexOfLongDay() != null && newShift.getCompliance().getIndexOfLongDay() == null) {
            notifyItemRemoved(rowNumberLongDays);
            rowNumberLongDays = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            if (rowNumberNights != RecyclerView.NO_POSITION)
                rowNumberNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getIndexOfLongDay() != null && newShift.getCompliance().getIndexOfLongDay() != null && (
                oldShift.getCompliance().getIndexOfLongDay().longValue() != newShift.getCompliance().getIndexOfLongDay().longValue() ||
                        !Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumLongDaysPerWeek(), newShift.getCompliance().compliesWithMaximumLongDaysPerWeek())
        )) {
            notifyItemChanged(rowNumberLongDays);
        }
        if (oldShift.getCompliance().getIndexOfNightShift() == null && newShift.getCompliance().getIndexOfNightShift() != null) {
            int lastPosition = rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            rowNumberNights = ++lastPosition;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberNights);
        } else if (oldShift.getCompliance().getIndexOfNightShift() != null && newShift.getCompliance().getIndexOfNightShift() == null) {
            notifyItemRemoved(rowNumberNights);
            rowNumberNights = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            if (rowNumberRecoveryAfterNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getIndexOfNightShift() != null && newShift.getCompliance().getIndexOfNightShift() != null && (
                oldShift.getCompliance().getIndexOfNightShift().longValue() != newShift.getCompliance().getIndexOfNightShift().longValue() ||
                        !Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumConsecutiveNightsWorked(), newShift.getCompliance().compliesWithMaximumConsecutiveNightsWorked())
        )) {
            notifyItemChanged(rowNumberNights);
        }
        if (oldShift.getCompliance().getRecoveryInformation() == null && newShift.getCompliance().getRecoveryInformation() != null) {
            int lastPosition = rowNumberNights != RecyclerView.NO_POSITION ? rowNumberNights : rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            rowNumberRecoveryAfterNights = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberRecoveryAfterNights);
        } else if (oldShift.getCompliance().getRecoveryInformation() != null && newShift.getCompliance().getRecoveryInformation() == null) {
            notifyItemRemoved(rowNumberRecoveryAfterNights);
            rowNumberRecoveryAfterNights = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberNights != RecyclerView.NO_POSITION ? rowNumberNights : rowNumberLongDays != RecyclerView.NO_POSITION ? rowNumberLongDays : rowNumberLastWeekendWorked != RecyclerView.NO_POSITION ? rowNumberLastWeekendWorked : rowNumberDurationWorkedOverFortnight;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getRecoveryInformation() != null && newShift.getCompliance().getRecoveryInformation() != null && (
                !oldShift.getCompliance().getRecoveryInformation().isEqualTo(newShift.getCompliance().getRecoveryInformation()) ||
                        !Comparators.equalCompliance(oldShift.getCompliance().sufficientRecoveryFollowingNights(), newShift.getCompliance().sufficientRecoveryFollowingNights())
        )) {
            notifyItemChanged(rowNumberRecoveryAfterNights);
        }
        if (BuildConfig.DEBUG) {
            logAllRowNumbers();
        }
    }

    @Override
    void onBindViewHolder(@NonNull RosteredShift shift, int position, @NonNull ItemViewHolder holder) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onBindViewHolder() called with: shift = [" + shift.getShiftData() + "], position = [" + position + "], holder = [" + holder + "]");
        }
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
            holder.setCompliant(shift.getCompliance().previousWeekendFree());
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
        } else if (position == rowNumberNights) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_consecutive_nights_black_24dp);
            holder.setCompliant(shift.getCompliance().compliesWithMaximumConsecutiveNightsWorked());
            //noinspection ConstantConditions
            int nights = shift.getCompliance().getIndexOfNightShift().intValue() + 1;
            holder.setText(getContext().getString(R.string.number_of_consecutive_nights_worked), getContext().getResources().getQuantityString(R.plurals.nights, nights, nights));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.showMessage(getContext().getString(R.string.maximum_consecutive_nights, Compliance.getMaximumConsecutiveNights(getSaferRostersOptions())));
                }
            });
        } else if (position == rowNumberRecoveryAfterNights) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_recovery_days_following_nights);
            holder.setCompliant(shift.getCompliance().sufficientRecoveryFollowingNights());
            //noinspection ConstantConditions
            int days = (int) shift.getCompliance().getRecoveryInformation().recoveryDaysFollowingNights;
            final long nights = shift.getCompliance().getRecoveryInformation().previousConsecutiveNightShifts;
            holder.setText(getContext().getString(R.string.recovery_days_following_nights), getContext().getString(R.string.n_days_following_n_nights, getContext().getResources().getQuantityString(R.plurals.days, days, days), getContext().getResources().getQuantityString(R.plurals.nights, (int) nights, (int) nights)));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Compliance.Configuration.SaferRostersOptions saferRostersOptions = getSaferRostersOptions();
                    int minimumNightsBeforeRecovery = Compliance.getMinimumNightsBeforeRecovery(saferRostersOptions);
                    if (nights < minimumNightsBeforeRecovery) {
                        callbacks.showMessage(getContext().getString(R.string.recovery_days_only_required_following_n_nights, getContext().getResources().getQuantityString(R.plurals.nights, minimumNightsBeforeRecovery, minimumNightsBeforeRecovery)));
                    } else {
                        int minimumRecoveryDaysFollowingNights = Compliance.getMinimumRecoveryDaysFollowingNights(saferRostersOptions, nights);
                        final String nNights;
                        if (saferRostersOptions == null) {
                            nNights = getContext().getString(R.string.n_or_more_nights, minimumNightsBeforeRecovery);
                        } else if (nights == 2 || nights == 3) {
                            nNights = getContext().getResources().getQuantityString(R.plurals.nights, (int) nights, (int) nights);
                        } else if (nights > 3) {
                            nNights = getContext().getString(R.string.n_or_more_nights, 4);
                        } else throw new IllegalStateException();
                        callbacks.showMessage(getContext().getString(R.string.minimum_n_recovery_days_following_n_nights, getContext().getResources().getQuantityString(R.plurals.days, minimumRecoveryDaysFollowingNights, minimumRecoveryDaysFollowingNights), nNights));
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

    @Nullable
    private Compliance.Configuration.SaferRostersOptions getSaferRostersOptions() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getBoolean(getContext().getString(R.string.key_safer_rosters), getContext().getResources().getBoolean(R.bool.default_safer_rosters)) ? new Compliance.Configuration.SaferRostersOptions(
                sharedPreferences.getBoolean(getContext().getString(R.string.key_allow_5_consecutive_nights), getContext().getResources().getBoolean(R.bool.default_allow_5_consecutive_nights)),
                sharedPreferences.getBoolean(getContext().getString(R.string.key_allow_only_1_recovery_day_following_3_nights), getContext().getResources().getBoolean(R.bool.default_allow_only_1_recovery_day_following_3_nights))
        ) : null;
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, DateDetailAdapterHelper.Callbacks {
        void setLogged(boolean logged);
        void changeTime(boolean start, boolean logged);
        void showMessage(@NonNull String message);
    }

}
