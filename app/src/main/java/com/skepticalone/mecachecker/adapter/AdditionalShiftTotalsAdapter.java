package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.util.ShiftUtil;

import java.util.List;

public final class AdditionalShiftTotalsAdapter extends PayableTotalsAdapter<AdditionalShiftEntity> {

    private static final int
            ROW_NUMBER_ALL_SHIFT_TOTAL = 0,
            ROW_NUMBER_NORMAL_DAY_TOTAL = 1,
            ROW_NUMBER_LONG_DAY_TOTAL = 2,
            ROW_NUMBER_NIGHT_SHIFT_TOTAL = 3,
            ROW_NUMBER_CUSTOM_SHIFT_TOTAL = 4,
            ROW_NUMBER_ALL_SHIFT_PAYMENT = 5,
            ROW_NUMBER_NORMAL_DAY_PAYMENT = 6,
            ROW_NUMBER_LONG_DAY_PAYMENT = 7,
            ROW_NUMBER_NIGHT_SHIFT_PAYMENT = 8,
            ROW_NUMBER_CUSTOM_SHIFT_PAYMENT = 9,
            ROW_COUNT = 10;

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
            case ROW_NUMBER_NORMAL_DAY_TOTAL:
            case ROW_NUMBER_NORMAL_DAY_PAYMENT:
                shiftType = ShiftUtil.ShiftType.NORMAL_DAY;
                break;
            case ROW_NUMBER_LONG_DAY_TOTAL:
            case ROW_NUMBER_LONG_DAY_PAYMENT:
                shiftType = ShiftUtil.ShiftType.LONG_DAY;
                break;
            case ROW_NUMBER_NIGHT_SHIFT_TOTAL:
            case ROW_NUMBER_NIGHT_SHIFT_PAYMENT:
                shiftType = ShiftUtil.ShiftType.NIGHT_SHIFT;
                break;
            case ROW_NUMBER_CUSTOM_SHIFT_TOTAL:
            case ROW_NUMBER_CUSTOM_SHIFT_PAYMENT:
                shiftType = ShiftUtil.ShiftType.CUSTOM;
                break;
            default:
                return super.bindViewHolder(allShifts, holder, position);
        }
        switch (position) {
            case ROW_NUMBER_NORMAL_DAY_TOTAL:
            case ROW_NUMBER_LONG_DAY_TOTAL:
            case ROW_NUMBER_NIGHT_SHIFT_TOTAL:
            case ROW_NUMBER_CUSTOM_SHIFT_TOTAL:
                bindTotalNumber(
                        ShiftUtil.getShiftIcon(shiftType),
                        ShiftUtil.getShiftTitle(shiftType),
                        calculator.getFilteredShifts(allShifts, shiftType),
                        holder
                );
                break;
            case ROW_NUMBER_NORMAL_DAY_PAYMENT:
            case ROW_NUMBER_LONG_DAY_PAYMENT:
            case ROW_NUMBER_NIGHT_SHIFT_PAYMENT:
            case ROW_NUMBER_CUSTOM_SHIFT_PAYMENT:
                bindTotalPayment(
                        ShiftUtil.getShiftIcon(shiftType),
                        ShiftUtil.getShiftTitle(shiftType),
                        calculator.getFilteredShifts(allShifts, shiftType),
                        holder
                );
                break;
        }
        return true;
    }

}
