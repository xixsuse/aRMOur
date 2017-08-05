package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.util.ShiftUtil;

import java.util.List;

public final class AdditionalShiftTotalsAdapter extends PayableTotalsAdapter<AdditionalShiftEntity> implements ShiftTotalsAdapterHelper.Callbacks<AdditionalShiftEntity> {

    private static final int
            ROW_NUMBER_NORMAL_DAY_TOTAL = 0,
            ROW_NUMBER_LONG_DAY_TOTAL = 1,
            ROW_NUMBER_NIGHT_SHIFT_TOTAL = 2,
            ROW_NUMBER_CUSTOM_SHIFT_TOTAL = 3,
            ROW_NUMBER_ALL_SHIFT_TOTAL = 4,
            ROW_NUMBER_NORMAL_DAY_PAYMENT = 5,
            ROW_NUMBER_LONG_DAY_PAYMENT = 6,
            ROW_NUMBER_NIGHT_SHIFT_PAYMENT = 7,
            ROW_NUMBER_CUSTOM_SHIFT_PAYMENT = 8,
            ROW_NUMBER_ALL_SHIFT_PAYMENT = 9,
            ROW_COUNT = 10;

    private final ShiftTotalsAdapterHelper<AdditionalShiftEntity> helper;

    AdditionalShiftTotalsAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks);
        helper = new ShiftTotalsAdapterHelper<>(this, calculator);
    }

    @Override
    int getTitle() {
        return R.string.additional_shifts;
    }

    @Override
    int getRowNumberTotalNumber() {
        return ROW_NUMBER_ALL_SHIFT_TOTAL;
    }

    @Override
    int getRowNumberTotalPayment() {
        return ROW_NUMBER_ALL_SHIFT_PAYMENT;
    }

    @Override
    public int getRowNumberNormalDayTotal() {
        return ROW_NUMBER_NORMAL_DAY_TOTAL;
    }

    @Override
    public int getRowNumberLongDayTotal() {
        return ROW_NUMBER_LONG_DAY_TOTAL;
    }

    @Override
    public int getRowNumberNightShiftTotal() {
        return ROW_NUMBER_NIGHT_SHIFT_TOTAL;
    }

    @Override
    public int getRowNumberCustomShiftTotal() {
        return ROW_NUMBER_CUSTOM_SHIFT_TOTAL;
    }

    @Override
    public int getRowNumberNormalDayPayment() {
        return ROW_NUMBER_NORMAL_DAY_PAYMENT;
    }

    @Override
    public int getRowNumberLongDayPayment() {
        return ROW_NUMBER_LONG_DAY_PAYMENT;
    }

    @Override
    public int getRowNumberNightShiftPayment() {
        return ROW_NUMBER_NIGHT_SHIFT_PAYMENT;
    }

    @Override
    public int getRowNumberCustomShiftPayment() {
        return ROW_NUMBER_CUSTOM_SHIFT_PAYMENT;
    }

    @Override
    int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    boolean bindViewHolder(@NonNull List<AdditionalShiftEntity> allItems, @NonNull ItemViewHolder holder, int position) {
        return helper.bindViewHolder(allItems, holder, position) || super.bindViewHolder(allItems, holder, position);
    }

}
