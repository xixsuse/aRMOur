package com.skepticalone.armour.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.ShiftUtil;

import org.joda.time.Duration;

import java.util.List;

final class ShiftTotalsAdapterHelper<Entity extends Shift> {

    static final int ROW_COUNT = 5;
    private static final int
            ROW_NUMBER_ALL_SHIFTS = 0,
            ROW_NUMBER_NORMAL_DAY = 1,
            ROW_NUMBER_LONG_DAY = 2,
            ROW_NUMBER_NIGHT_SHIFT = 3,
            ROW_NUMBER_CUSTOM_SHIFT = 4;
    @NonNull
    private final Callbacks<Entity> callbacks;
    @NonNull
    private final ShiftUtil.Calculator calculator;

    ShiftTotalsAdapterHelper(@NonNull Callbacks<Entity> callbacks, @NonNull ShiftUtil.Calculator calculator) {
        this.callbacks = callbacks;
        this.calculator = calculator;
    }

    @NonNull
    private String getTotalDuration(@NonNull List<Entity> shifts, @NonNull ItemViewHolder holder) {
        Duration totalDuration = Duration.ZERO;
        for (Entity shift : shifts) {
            totalDuration = totalDuration.plus(shift.getShiftData().getDuration());
        }
        if (callbacks.isFiltered() && !totalDuration.isEqual(Duration.ZERO)) {
            Duration filteredDuration = Duration.ZERO;
            for (Entity shift : shifts) {
                if (callbacks.isIncluded(shift)) filteredDuration = filteredDuration.plus(shift.getShiftData().getDuration());
            }
            return holder.getDurationPercentage(filteredDuration, totalDuration);
        } else {
            return holder.getDurationString(totalDuration);
        }
    }

    boolean bindViewHolder(@NonNull List<Entity> allShifts, @NonNull ItemViewHolder holder, int position) {
        @DrawableRes final int icon;
        @StringRes final int firstLine;
        @NonNull final List<Entity> shifts;
        if (position == ROW_NUMBER_ALL_SHIFTS) {
            icon = R.drawable.ic_sigma_black_24dp;
            firstLine = callbacks.getAllShiftsTitle();
            shifts = allShifts;
        } else {
            final ShiftUtil.ShiftType shiftType;
            switch (position) {
                case ROW_NUMBER_NORMAL_DAY:
                    shiftType = ShiftUtil.ShiftType.NORMAL_DAY;
                    firstLine = R.string.normal_days;
                    break;
                case ROW_NUMBER_LONG_DAY:
                    shiftType = ShiftUtil.ShiftType.LONG_DAY;
                    firstLine = R.string.long_days;
                    break;
                case ROW_NUMBER_NIGHT_SHIFT:
                    shiftType = ShiftUtil.ShiftType.NIGHT_SHIFT;
                    firstLine = R.string.night_shifts;
                    break;
                case ROW_NUMBER_CUSTOM_SHIFT:
                    shiftType = ShiftUtil.ShiftType.CUSTOM;
                    firstLine = R.string.custom_shifts;
                    break;
                default:
                    return false;
            }
            icon = ShiftUtil.getShiftIcon(shiftType);
            shifts = calculator.getFilteredShifts(allShifts, shiftType);
        }
        holder.setupTotals(icon, firstLine, callbacks.getTotalNumber(shifts, holder), callbacks.getThirdLine(getTotalDuration(shifts, holder), shifts, holder));
        return true;
    }

    interface Callbacks<Entity extends Shift> extends TotalsAdapterCallbacks<Entity> {
        @StringRes int getAllShiftsTitle();
        @NonNull String getThirdLine(@NonNull String totalDuration, @NonNull List<Entity> shifts, @NonNull ItemViewHolder holder);
    }

}
