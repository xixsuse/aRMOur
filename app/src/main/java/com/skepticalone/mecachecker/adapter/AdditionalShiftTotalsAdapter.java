package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.util.ShiftUtil;

import java.util.List;

public final class AdditionalShiftTotalsAdapter extends PayableTotalsAdapter<AdditionalShiftEntity> {

    private static final int
            ROW_NUMBER_ALL_SHIFT_TOTAL = 0,
            ROW_NUMBER_ALL_SHIFT_PAYMENT = 1,
            ROW_NUMBER_NORMAL_DAY = 2,
            ROW_NUMBER_LONG_DAY = 3,
            ROW_NUMBER_NIGHT_SHIFT = 4,
            ROW_NUMBER_CUSTOM_SHIFT = 5,
            ROW_COUNT = 6;

    @NonNull
    private final ShiftUtil.Calculator calculator;

    public AdditionalShiftTotalsAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks);
        this.calculator = calculator;
    }

    @Override
    int getTotalNumberTitle() {
        return R.string.all_shifts;
    }

    @Override
    int getTotalPaymentTitle() {
        return getTotalNumberTitle();
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
    int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    boolean bindViewHolder(@NonNull List<AdditionalShiftEntity> allShifts, @NonNull ItemViewHolder holder, int position) {
        final ShiftUtil.ShiftType shiftType;
        switch (position) {
            case ROW_NUMBER_NORMAL_DAY:
                shiftType = ShiftUtil.ShiftType.NORMAL_DAY;
                break;
            case ROW_NUMBER_LONG_DAY:
                shiftType = ShiftUtil.ShiftType.LONG_DAY;
                break;
            case ROW_NUMBER_NIGHT_SHIFT:
                shiftType = ShiftUtil.ShiftType.NIGHT_SHIFT;
                break;
            case ROW_NUMBER_CUSTOM_SHIFT:
                shiftType = ShiftUtil.ShiftType.CUSTOM;
                break;
            default:
                return super.bindViewHolder(allShifts, holder, position);
        }
        List<AdditionalShiftEntity> filteredShifts = calculator.getFilteredShifts(allShifts, shiftType);
        holder.setupTotals(
                ShiftUtil.getShiftIcon(shiftType),
                ShiftUtil.getShiftTitle(shiftType),
                getTotalNumber(filteredShifts, holder) + '\n' + getTotalPayment(filteredShifts, holder)
        );
        return true;
    }

}
