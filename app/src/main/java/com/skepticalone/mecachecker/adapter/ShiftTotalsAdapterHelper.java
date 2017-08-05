package com.skepticalone.mecachecker.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Shift;
import com.skepticalone.mecachecker.util.ShiftUtil;

import java.util.List;

final class ShiftTotalsAdapterHelper<Entity extends Shift> {

    private static final int
            ROW_NUMBER_ALL_SHIFTS = 0,
            ROW_NUMBER_NORMAL_DAY = 1,
            ROW_NUMBER_LONG_DAY = 2,
            ROW_NUMBER_NIGHT_SHIFT = 3,
            ROW_NUMBER_CUSTOM_SHIFT = 4;
    static final int ROW_COUNT = 5;

    @NonNull
    private final Callbacks<Entity> callbacks;
    @NonNull
    private final ShiftUtil.Calculator calculator;

    ShiftTotalsAdapterHelper(@NonNull Callbacks<Entity> callbacks, @NonNull ShiftUtil.Calculator calculator) {
        this.callbacks = callbacks;
        this.calculator = calculator;
    }

    boolean bindViewHolder(@NonNull List<Entity> allShifts, @NonNull ItemViewHolder holder, int position) {
        @DrawableRes final int icon;
        @StringRes final int firstLine;
        @NonNull final List<Entity> shifts;
        if (position == ROW_NUMBER_ALL_SHIFTS) {
            icon = R.drawable.ic_list_black_24dp;
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
        holder.setupTotals(icon, firstLine, callbacks.getSecondLine(shifts, holder), callbacks.getThirdLine(shifts, holder));
        return true;
    }

    interface Callbacks<Entity extends Shift> {
        @StringRes int getAllShiftsTitle();
        @NonNull String getSecondLine(@NonNull List<Entity> shifts, @NonNull ItemViewHolder holder);
        @NonNull String getThirdLine(@NonNull List<Entity> shifts, @NonNull ItemViewHolder holder);
    }

}
