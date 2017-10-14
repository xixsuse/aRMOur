package com.skepticalone.armour.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;

import java.util.ArrayList;
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

    ShiftTotalsAdapterHelper(@NonNull Callbacks<Entity> callbacks) {
        this.callbacks = callbacks;
    }

    @NonNull
    private String getTotalDuration(@NonNull ItemTotalsAdapter adapter, @NonNull List<Entity> shifts) {
        Duration totalDuration = Duration.ZERO;
        for (Entity shift : shifts) {
            totalDuration = totalDuration.plus(shift.getShiftData().getDuration());
        }
        if (callbacks.isFiltered() && !totalDuration.equals(Duration.ZERO)) {
            Duration filteredDuration = Duration.ZERO;
            for (Entity shift : shifts) {
                if (callbacks.isIncluded(shift))
                    filteredDuration = filteredDuration.plus(shift.getShiftData().getDuration());
            }
            return adapter.getDurationPercentage(filteredDuration, totalDuration);
        } else {
            return DateTimeUtils.getDurationString(adapter.getContext(), totalDuration);
        }
    }

    boolean bindViewHolder(@NonNull ItemTotalsAdapter adapter, @NonNull List<Entity> allShifts, @NonNull ItemViewHolder holder, int position) {
        @DrawableRes final int icon;
        @StringRes final int firstLine;
        @NonNull final List<Entity> shifts;
        if (position == ROW_NUMBER_ALL_SHIFTS) {
            icon = R.drawable.ic_sigma_black_24dp;
            firstLine = callbacks.getAllShiftsTitle();
            shifts = allShifts;
        } else {
            Shift.ShiftType shiftType;
            if (position == ROW_NUMBER_NORMAL_DAY) shiftType = Shift.ShiftType.NORMAL_DAY;
            else if (position == ROW_NUMBER_LONG_DAY) shiftType = Shift.ShiftType.LONG_DAY;
            else if (position == ROW_NUMBER_NIGHT_SHIFT) shiftType = Shift.ShiftType.NIGHT_SHIFT;
            else if (position == ROW_NUMBER_CUSTOM_SHIFT) shiftType = Shift.ShiftType.CUSTOM;
            else return false;
            icon = shiftType.getIcon();
            firstLine = shiftType.getPluralTitle();
            shifts = new ArrayList<>();
            for (Entity shift : allShifts) {
                if (shift.getShiftType() == shiftType) shifts.add(shift);
            }
        }
        holder.setPrimaryIcon(icon);
        holder.setText(adapter.getContext().getString(firstLine), callbacks.getTotalNumber(shifts), callbacks.getThirdLine(getTotalDuration(adapter, shifts), shifts));
        return true;
    }

    interface Callbacks<Entity extends Shift> extends TotalsAdapterCallbacks<Entity> {
        @StringRes int getAllShiftsTitle();

        @NonNull
        String getThirdLine(@NonNull String totalDuration, @NonNull List<Entity> shifts);
    }

}
