package com.skepticalone.armour.adapter;

import android.content.Context;
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
            rowNumberNightsOrRecovery,
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
            rowNumberNightsOrRecovery = rosteredShift.getCompliance().getIndexOfNightShift() == null && rosteredShift.getCompliance().getRecoveryDaysFollowingNights() == null ? RecyclerView.NO_POSITION : ++lastPosition;
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
            if (rowNumberNightsOrRecovery != RecyclerView.NO_POSITION)
                rowNumberNightsOrRecovery = ++lastPosition;
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
            if (rowNumberNightsOrRecovery != RecyclerView.NO_POSITION)
                rowNumberNightsOrRecovery = ++lastPosition;
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
            if (rowNumberNightsOrRecovery != RecyclerView.NO_POSITION)
                rowNumberNightsOrRecovery = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberLastWeekendWorked);
        } else if (oldShift.getCompliance().getCurrentWeekend() != null && newShift.getCompliance().getCurrentWeekend() == null) {
            notifyItemRemoved(rowNumberLastWeekendWorked);
            rowNumberLastWeekendWorked = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberDurationWorkedOverFortnight;
            if (rowNumberNightsOrRecovery != RecyclerView.NO_POSITION)
                rowNumberNightsOrRecovery = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getCurrentWeekend() != null && newShift.getCompliance().getCurrentWeekend() != null && (
                !oldShift.getCompliance().getCurrentWeekend().isEqual(newShift.getCompliance().getCurrentWeekend()) ||
                        !Comparators.equalDates(oldShift.getCompliance().getLastWeekendWorked(), newShift.getCompliance().getLastWeekendWorked()) ||
                        !Comparators.equalCompliance(oldShift.getCompliance().previousWeekendFree(), newShift.getCompliance().previousWeekendFree())
        )) {
            notifyItemChanged(rowNumberLastWeekendWorked);
        }
        if (oldShift.getCompliance().getIndexOfNightShift() == null && oldShift.getCompliance().getRecoveryDaysFollowingNights() == null && (newShift.getCompliance().getIndexOfNightShift() != null || newShift.getCompliance().getRecoveryDaysFollowingNights() != null)) {
            int lastPosition = rowNumberLastWeekendWorked == RecyclerView.NO_POSITION ? rowNumberDurationWorkedOverFortnight : rowNumberLastWeekendWorked;
            rowNumberNightsOrRecovery = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberNightsOrRecovery);
        } else if (newShift.getCompliance().getIndexOfNightShift() == null && newShift.getCompliance().getRecoveryDaysFollowingNights() == null && (oldShift.getCompliance().getIndexOfNightShift() != null || oldShift.getCompliance().getRecoveryDaysFollowingNights() != null)) {
            notifyItemRemoved(rowNumberNightsOrRecovery);
            rowNumberNightsOrRecovery = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberLastWeekendWorked == RecyclerView.NO_POSITION ? rowNumberDurationWorkedOverFortnight : rowNumberLastWeekendWorked;
            rowCount = ++lastPosition;
        } else if (
                (((oldShift.getCompliance().getIndexOfNightShift() == null) != (newShift.getCompliance().getIndexOfNightShift() == null)) && ((oldShift.getCompliance().getRecoveryDaysFollowingNights() == null) != (newShift.getCompliance().getRecoveryDaysFollowingNights() == null))) ||
                        (oldShift.getCompliance().getIndexOfNightShift() != null && newShift.getCompliance().getIndexOfNightShift() != null && (
                                oldShift.getCompliance().getIndexOfNightShift().longValue() != newShift.getCompliance().getIndexOfNightShift().longValue() ||
                                        !Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumConsecutiveNightsWorked(), newShift.getCompliance().compliesWithMaximumConsecutiveNightsWorked())
                        )) ||
                        (oldShift.getCompliance().getRecoveryDaysFollowingNights() != null && newShift.getCompliance().getRecoveryDaysFollowingNights() != null && (
                                oldShift.getCompliance().getRecoveryDaysFollowingNights().longValue() != newShift.getCompliance().getRecoveryDaysFollowingNights().longValue() ||
                                        !Comparators.equalCompliance(oldShift.getCompliance().adequateRecovery(), newShift.getCompliance().adequateRecovery())
                        ))
                ) {
            notifyItemChanged(rowNumberNightsOrRecovery);
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
        } else if (position == rowNumberNightsOrRecovery) {
            holder.setupPlain();
            if (shift.getCompliance().getIndexOfNightShift() != null) {
                // TODO: 22/10/17 remove this
                if (shift.getCompliance().getRecoveryDaysFollowingNights() != null)
                    throw new IllegalStateException();
                holder.setPrimaryIcon(R.drawable.ic_consecutive_nights_black_24dp);
                holder.setCompliant(shift.getCompliance().compliesWithMaximumConsecutiveNightsWorked());
                holder.setText("Number of consecutive nights worked", Long.toString(shift.getCompliance().getIndexOfNightShift() + 1));
                // TODO: 22/10/17 get number dynamically
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callbacks.showMessage("No more than " + Compliance.DEFAULT_MAXIMUM_CONSECUTIVE_NIGHTS_WORKED + " consecutive nights are allowed");
                    }
                });
            } else if (shift.getCompliance().getRecoveryDaysFollowingNights() != null) {
                // TODO: 22/10/17 remove this
                if (shift.getCompliance().getIndexOfNightShift() != null)
                    throw new IllegalStateException();
                holder.setPrimaryIcon(R.drawable.ic_recovery_days_following_nights);
                holder.setCompliant(shift.getCompliance().adequateRecovery());
                int days = shift.getCompliance().getRecoveryDaysFollowingNights().intValue();
                holder.setText("Recovery following nights", getContext().getResources().getQuantityString(R.plurals.days, days, days));
                // TODO: 22/10/17 get number dynamically
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callbacks.showMessage("Should be X days recovery following Y nights");
                    }
                });
            } else throw new IllegalStateException();
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
