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

abstract class ShiftTotalsAdapterHelper<FinalItem extends Shift> {

    static final int ROW_COUNT = 5;
    private static final int
            ROW_NUMBER_ALL_SHIFTS = 0,
            ROW_NUMBER_NORMAL_DAY = 1,
            ROW_NUMBER_LONG_DAY = 2,
            ROW_NUMBER_NIGHT_SHIFT = 3,
            ROW_NUMBER_CUSTOM_SHIFT = 4;

    @NonNull
    private String getTotalDuration(@NonNull FilteredItemTotalsAdapter<FinalItem> adapter, @NonNull List<FinalItem> shifts) {
        Duration totalDuration = Duration.ZERO;
        for (FinalItem shift : shifts) {
            totalDuration = totalDuration.plus(shift.getShiftData().getDuration());
        }
        if (adapter.isFiltered() && !totalDuration.equals(Duration.ZERO)) {
            Duration filteredDuration = Duration.ZERO;
            for (FinalItem shift : shifts) {
                if (adapter.isIncluded(shift))
                    filteredDuration = filteredDuration.plus(shift.getShiftData().getDuration());
            }
            return adapter.getDurationPercentage(filteredDuration, totalDuration);
        } else {
            return DateTimeUtils.getDurationString(adapter.getContext(), totalDuration);
        }
    }

    void onBindViewHolder(@NonNull List<FinalItem> allShifts, int position, @NonNull ItemViewHolder holder, @NonNull FilteredItemTotalsAdapter<FinalItem> adapter) {
        @DrawableRes final int icon;
        @StringRes final int firstLine;
        @NonNull final List<FinalItem> shifts;
        if (position == ROW_NUMBER_ALL_SHIFTS) {
            icon = R.drawable.ic_sigma_black_24dp;
            firstLine = adapter.getTotalItemsTitle();
            shifts = allShifts;
        } else {
            Shift.ShiftType shiftType;
            if (position == ROW_NUMBER_NORMAL_DAY) shiftType = Shift.ShiftType.NORMAL_DAY;
            else if (position == ROW_NUMBER_LONG_DAY) shiftType = Shift.ShiftType.LONG_DAY;
            else if (position == ROW_NUMBER_NIGHT_SHIFT) shiftType = Shift.ShiftType.NIGHT_SHIFT;
            else if (position == ROW_NUMBER_CUSTOM_SHIFT) shiftType = Shift.ShiftType.CUSTOM;
            else return;
            icon = shiftType.getIcon();
            firstLine = shiftType.getPluralTitle();
            shifts = new ArrayList<>();
            for (FinalItem shift : allShifts) {
                if (shift.getShiftType() == shiftType) shifts.add(shift);
            }
        }
        holder.setPrimaryIcon(icon);
        holder.setText(adapter.getContext().getString(firstLine), adapter.getTotalNumber(shifts), getThirdLine(getTotalDuration(adapter, shifts), shifts));
    }

    @NonNull
    abstract String getThirdLine(@NonNull String totalDuration, @NonNull List<FinalItem> shifts);

}
