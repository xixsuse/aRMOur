package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftDetailAdapter extends PayableDetailAdapter<AdditionalShiftEntity> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_SHIFT_TYPE = 3,
            ROW_NUMBER_PAYMENT = 4,
            ROW_NUMBER_COMMENT = 5,
            ROW_NUMBER_CLAIMED = 6,
            ROW_NUMBER_PAID = 7,
            ROW_COUNT = 8;

    @NonNull
    private final ShiftDetailAdapterHelper<AdditionalShiftEntity> shiftDetailAdapterHelper;

    public AdditionalShiftDetailAdapter(@NonNull final Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks);
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper<AdditionalShiftEntity>(callbacks, calculator) {
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
                callbacks.changeTime(start);
            }

        };
    }

    @Override
    int getRowNumberPayment() {
        return ROW_NUMBER_PAYMENT;
    }

    @Override
    int getRowNumberClaimed() {
        return ROW_NUMBER_CLAIMED;
    }

    @Override
    int getRowNumberPaid() {
        return ROW_NUMBER_PAID;
    }

    @Override
    int getRowNumberComment(@NonNull AdditionalShiftEntity shift) {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getRowCount(@NonNull AdditionalShiftEntity shift) {
        return ROW_COUNT;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    void onItemUpdated(@NonNull AdditionalShiftEntity oldShift, @NonNull AdditionalShiftEntity newShift) {
        super.onItemUpdated(oldShift, newShift);
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
    }

    @Override
    boolean bindViewHolder(@NonNull AdditionalShiftEntity shift, ItemViewHolder holder, int position) {
        return shiftDetailAdapterHelper.bindViewHolder(shift, holder, position) ||
                super.bindViewHolder(shift, holder, position);
    }

    public interface Callbacks extends PayableDetailAdapter.Callbacks, DateDetailAdapterHelper.Callbacks {
        void changeTime(boolean start);
    }

}
