package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftListAdapter extends ItemListAdapter<AdditionalShiftEntity> {

    @NonNull
    private final ShiftUtil.Calculator calculator;

    public AdditionalShiftListAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks);
        this.calculator = calculator;
    }

    @Override
    boolean areContentsTheSame(@NonNull AdditionalShiftEntity shift1, @NonNull AdditionalShiftEntity shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                Comparators.equalPaymentData(shift1.getPaymentData(), shift2.getPaymentData()) &&
                Comparators.equalShiftData(shift1.getShiftData(), shift2.getShiftData());
    }

    @Override
    void bindViewHolder(@NonNull AdditionalShiftEntity shift, ItemViewHolder holder) {
        holder.primaryIcon.setImageResource(ShiftUtil.getShiftIcon(calculator.getShiftType(shift.getShiftData())));
        holder.secondaryIcon.setImageResource(shift.getPaymentData().getIcon());
        holder.setText(
                DateTimeUtils.getFullDateString(shift.getShiftData().getStart().toLocalDate()),
                DateTimeUtils.getTimeSpanString(shift.getShiftData()),
                shift.getComment()
        );
    }

}
