package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Shift;
import com.skepticalone.mecachecker.util.ShiftUtil;

import java.util.ArrayList;
import java.util.List;

final class ShiftTotalsAdapterHelper<Entity extends Shift> {

    @NonNull
    private final Callbacks<Entity> callbacks;
    @NonNull
    private final ShiftUtil.Calculator calculator;

    ShiftTotalsAdapterHelper(@NonNull Callbacks<Entity> callbacks, @NonNull ShiftUtil.Calculator calculator) {
        this.callbacks = callbacks;
        this.calculator = calculator;
    }

    @NonNull
    private List<Entity> getFilteredShiftList(@NonNull List<Entity> allShifts, @NonNull ShiftUtil.Filter filter) {
        List<Entity> shifts = new ArrayList<>();
        for (Entity shift : allShifts) {
            if (filter.include(shift.getShiftData())) shifts.add(shift);
        }
        return shifts;
    }

    final boolean bindViewHolder(@NonNull List<Entity> shifts, @NonNull ItemViewHolder holder, int position) {
        if (position == callbacks.getRowNumberNormalDayTotal()) {
            callbacks.bindTotalNumber(R.drawable.ic_normal_day_black_24dp, R.string.normal_day, getFilteredShiftList(shifts, calculator.normalDayFilter), holder);
        } else if (position == callbacks.getRowNumberLongDayTotal()) {
            callbacks.bindTotalNumber(R.drawable.ic_long_day_black_24dp, R.string.long_day, getFilteredShiftList(shifts, calculator.longDayFilter), holder);
        } else if (position == callbacks.getRowNumberNightShiftTotal()) {
            callbacks.bindTotalNumber(R.drawable.ic_night_shift_black_24dp, R.string.night_shift, getFilteredShiftList(shifts, calculator.nightShiftFilter), holder);
        } else if (position == callbacks.getRowNumberCustomShiftTotal()) {
            callbacks.bindTotalNumber(R.drawable.ic_custom_shift_black_24dp, R.string.custom_shift, getFilteredShiftList(shifts, calculator.customShiftFilter), holder);
        } else if (position == callbacks.getRowNumberNormalDayPayment()) {
            callbacks.bindTotalPayment(R.drawable.ic_normal_day_black_24dp, R.string.normal_day, getFilteredShiftList(shifts, calculator.normalDayFilter), holder);
        } else if (position == callbacks.getRowNumberLongDayPayment()) {
            callbacks.bindTotalPayment(R.drawable.ic_long_day_black_24dp, R.string.long_day, getFilteredShiftList(shifts, calculator.longDayFilter), holder);
        } else if (position == callbacks.getRowNumberNightShiftPayment()) {
            callbacks.bindTotalPayment(R.drawable.ic_night_shift_black_24dp, R.string.night_shift, getFilteredShiftList(shifts, calculator.nightShiftFilter), holder);
        } else if (position == callbacks.getRowNumberCustomShiftPayment()) {
            callbacks.bindTotalPayment(R.drawable.ic_custom_shift_black_24dp, R.string.custom_shift, getFilteredShiftList(shifts, calculator.customShiftFilter), holder);
        } else return false;
        return true;
    }

    public interface Callbacks {
        int getRowNumberNormalDayTotal();
        int getRowNumberLongDayTotal();
        int getRowNumberNightShiftTotal();
        int getRowNumberCustomShiftTotal();
        int getRowNumberNormalDayPayment();
        int getRowNumberLongDayPayment();
        int getRowNumberNightShiftPayment();
        int getRowNumberCustomShiftPayment();
//        void bindTotalNumber(@DrawableRes int icon, @StringRes int title, @NonNull List<T> shifts, @NonNull ItemViewHolder holder);
//        void bindTotalPayment(@DrawableRes int icon, @StringRes int title, @NonNull List<T> shifts, @NonNull ItemViewHolder holder);
    }
}
