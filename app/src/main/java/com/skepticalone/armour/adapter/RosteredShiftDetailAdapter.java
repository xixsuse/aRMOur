package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
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
            ROW_NUMBER_LOGGED_START = 5,
            ROW_NUMBER_LOGGED_END = 6,
            ROW_NUMBER_COMMENT_IF_LOGGED = 7,
            ROW_NUMBER_DURATION_BETWEEN_SHIFTS_IF_LOGGED = 8,
            ROW_NUMBER_DURATION_WORKED_OVER_DAY_IF_LOGGED = 9,
            ROW_NUMBER_DURATION_WORKED_OVER_WEEK_IF_LOGGED = 10,
            ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT_IF_LOGGED = 11,
            ROW_NUMBER_LAST_WEEKEND_WORKED_IF_LOGGED = 12,
            ROW_COUNT_IF_LOGGED = 13,
            NUMBER_OF_ROWS_FOR_LOGGED = 2;

    @NonNull
    private final ShiftDetailAdapterHelper<RosteredShift> shiftDetailAdapterHelper;

    @NonNull
    private final Callbacks callbacks;

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

    private static int adjustForLogged(int valueIfLogged, @NonNull RosteredShift shift) {
        return shift.getLoggedShiftData() == null ? valueIfLogged - NUMBER_OF_ROWS_FOR_LOGGED : valueIfLogged;
    }

    private static int getRowNumberDurationBetweenShifts(@NonNull RosteredShift shift) {
        return adjustForLogged(ROW_NUMBER_DURATION_BETWEEN_SHIFTS_IF_LOGGED, shift);
    }

    private static int getRowNumberDurationWorkedOverDay(@NonNull RosteredShift shift) {
        return adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_DAY_IF_LOGGED, shift);
    }

    private static int getRowNumberDurationWorkedOverWeek(@NonNull RosteredShift shift) {
        return adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_WEEK_IF_LOGGED, shift);
    }

    private static int getRowNumberDurationWorkedOverFortnight(@NonNull RosteredShift shift) {
        return adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT_IF_LOGGED, shift);
    }

    private static int getRowNumberLastWeekendWorked(@NonNull RosteredShift shift) {
        return adjustForLogged(ROW_NUMBER_LAST_WEEKEND_WORKED_IF_LOGGED, shift);
    }

    @Override
    int getRowNumberComment(@NonNull RosteredShift shift) {
        return adjustForLogged(ROW_NUMBER_COMMENT_IF_LOGGED, shift);
    }

    @Override
    int getItemCount(@NonNull RosteredShift shift) {
        int countIfWeekend = adjustForLogged(ROW_COUNT_IF_LOGGED, shift);
        return shift.getCompliance().getCurrentWeekend() == null ? --countIfWeekend : countIfWeekend;
    }

    @Override
    void onChanged(@NonNull RosteredShift oldShift, @NonNull RosteredShift newShift) {
        if (oldShift.getCompliance().getCurrentWeekend() == null && newShift.getCompliance().getCurrentWeekend() != null) {
            notifyItemInserted(getRowNumberLastWeekendWorked(oldShift));
        } else if (oldShift.getCompliance().getCurrentWeekend() != null && newShift.getCompliance().getCurrentWeekend() == null) {
            notifyItemRemoved(getRowNumberLastWeekendWorked(oldShift));
        } else if (oldShift.getCompliance().getCurrentWeekend() != null && newShift.getCompliance().getCurrentWeekend() != null && (
                !oldShift.getCompliance().getCurrentWeekend().isEqual(newShift.getCompliance().getCurrentWeekend()) ||
                        !Comparators.equalDates(oldShift.getCompliance().getLastWeekendWorked(), newShift.getCompliance().getLastWeekendWorked()) ||
                        !Comparators.equalCompliance(oldShift.getCompliance().previousWeekendFree(), newShift.getCompliance().previousWeekendFree())
        )) {
            notifyItemChanged(getRowNumberLastWeekendWorked(oldShift));
        }
        if (!Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumDurationOverFortnight(), newShift.getCompliance().compliesWithMaximumDurationOverFortnight()) || !oldShift.getCompliance().getDurationOverFortnight().equals(newShift.getCompliance().getDurationOverFortnight())) {
            notifyItemChanged(getRowNumberDurationWorkedOverFortnight(oldShift));
        }
        if (!Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumDurationOverWeek(), newShift.getCompliance().compliesWithMaximumDurationOverWeek()) || !oldShift.getCompliance().getDurationOverWeek().equals(newShift.getCompliance().getDurationOverWeek())) {
            notifyItemChanged(getRowNumberDurationWorkedOverWeek(oldShift));
        }
        if (!Comparators.equalCompliance(oldShift.getCompliance().compliesWithMaximumDurationOverDay(), newShift.getCompliance().compliesWithMaximumDurationOverDay()) || !oldShift.getCompliance().getDurationOverDay().equals(newShift.getCompliance().getDurationOverDay())) {
            notifyItemChanged(getRowNumberDurationWorkedOverDay(oldShift));
        }
        if (!Comparators.equalCompliance(oldShift.getCompliance().sufficientDurationBetweenShifts(), newShift.getCompliance().sufficientDurationBetweenShifts()) || !(oldShift.getCompliance().getDurationBetweenShifts() == null ? newShift.getCompliance().getDurationBetweenShifts() == null : (newShift.getCompliance().getDurationBetweenShifts() != null && oldShift.getCompliance().getDurationBetweenShifts().equals(newShift.getCompliance().getDurationBetweenShifts())))) {
            notifyItemChanged(getRowNumberDurationBetweenShifts(oldShift));
        }
        super.onChanged(oldShift, newShift);
        if (oldShift.getLoggedShiftData() == null && newShift.getLoggedShiftData() != null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            notifyItemRangeInserted(ROW_NUMBER_LOGGED_START, NUMBER_OF_ROWS_FOR_LOGGED);
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() == null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            notifyItemRangeRemoved(ROW_NUMBER_LOGGED_START, NUMBER_OF_ROWS_FOR_LOGGED);
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() != null) {
            if (!oldShift.getLoggedShiftData().getDuration().equals(newShift.getLoggedShiftData().getDuration())) {
                notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            }
            if (!oldShift.getLoggedShiftData().getStart().toLocalTime().equals(newShift.getLoggedShiftData().getStart().toLocalTime())) {
                notifyItemChanged(ROW_NUMBER_LOGGED_START);
            }
            if (!oldShift.getLoggedShiftData().getEnd().toLocalTime().equals(newShift.getLoggedShiftData().getEnd().toLocalTime()) || (oldShift.getShiftData().getStart().toLocalDate().isEqual(oldShift.getLoggedShiftData().getEnd().toLocalDate()) ? !newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getLoggedShiftData().getEnd().toLocalDate()) : (newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getLoggedShiftData().getEnd().toLocalDate()) || !oldShift.getLoggedShiftData().getEnd().getDayOfWeek().equals(newShift.getLoggedShiftData().getEnd().getDayOfWeek())))) {
                notifyItemChanged(ROW_NUMBER_LOGGED_END);
            }
        }
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
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
        } else if (position == ROW_NUMBER_LOGGED_START && shift.getLoggedShiftData() != null) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_clipboard_play_black_24dp);
            holder.hideSecondaryIcon();
            holder.setText(getContext().getString(R.string.logged_start), DateTimeUtils.getTimeString(shift.getLoggedShiftData().getStart().toLocalTime()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(true, true);
                }
            });
        } else if (position == ROW_NUMBER_LOGGED_END && shift.getLoggedShiftData() != null) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_clipboard_stop_black_24dp);
            holder.hideSecondaryIcon();
            holder.setText(getContext().getString(R.string.logged_end), DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getEnd().toLocalDateTime(), shift.getShiftData().getStart().toLocalDate()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(false, true);
                }
            });
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_BETWEEN_SHIFTS_IF_LOGGED, shift)) {
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
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_DAY_IF_LOGGED, shift)) {
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
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_WEEK_IF_LOGGED, shift)) {
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
        } else if (position == adjustForLogged(ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT_IF_LOGGED, shift)) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_fortnight_black_24dp);
            holder.setCompliant(shift.getCompliance().compliesWithMaximumDurationOverFortnight());
            holder.setText(getContext().getString(R.string.duration_worked_over_fortnight), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverFortnight()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_fortnight, AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT));
                }
            });
        } else if (shift.getCompliance().getCurrentWeekend() != null && position == adjustForLogged(ROW_NUMBER_LAST_WEEKEND_WORKED_IF_LOGGED, shift)) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_weekend_black_24dp);
            holder.setCompliant(shift.getCompliance().previousWeekendFree());
            final String secondLine, thirdLine;
            if (shift.getCompliance().getLastWeekendWorked() == null) {
                secondLine = getContext().getString(R.string.not_applicable);
                thirdLine = null;
            } else {
                secondLine = DateTimeUtils.getWeekendDateSpanString(shift.getCompliance().getLastWeekendWorked());
                thirdLine = DateTimeUtils.getWeeksAgo(getContext(), shift.getCompliance().getCurrentWeekend(), shift.getCompliance().getLastWeekendWorked());
            }
            holder.setText(getContext().getString(R.string.last_weekend_worked), secondLine, thirdLine);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_consecutive_weekends));
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
