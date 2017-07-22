package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.data.model.AdditionalShift;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftDetailAdapter extends ItemDetailAdapter<AdditionalShift> {

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

    private final ShiftDetailAdapterHelper<AdditionalShift> shiftDetailAdapterHelper;
    private final PayableDetailAdapterHelper payableDetailAdapterHelper;

    public AdditionalShiftDetailAdapter(final Callbacks callbacks, ShiftUtil.Calculator calculator) {
        super(callbacks);
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper<AdditionalShift>(calculator){
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
            void changeDate(@NonNull AdditionalShift shift) {
                callbacks.changeDate(shift.getId(), shift.getShiftData());
            }

            @Override
            void changeTime(@NonNull AdditionalShift shift, boolean isStart) {
                callbacks.changeTime(shift.getId(), isStart, shift.getShiftData());
            }
        };
        payableDetailAdapterHelper = new PayableDetailAdapterHelper(callbacks){
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
        };
    }

    @Override
    int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getRowCount(@NonNull AdditionalShift item) {
        return ROW_COUNT;
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    void onItemUpdated(@NonNull AdditionalShift oldShift, @NonNull AdditionalShift newShift) {
        super.onItemUpdated(oldShift, newShift);
        payableDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
        if (!Comparators.equalShiftData(oldShift.getShiftData(), newShift.getShiftData())) {
            notifyItemChanged(ROW_NUMBER_DATE);
            notifyItemChanged(ROW_NUMBER_START);
            notifyItemChanged(ROW_NUMBER_END);
            notifyItemChanged(ROW_NUMBER_SHIFT_TYPE);
        }
    }

    @Override
    boolean bindViewHolder(@NonNull AdditionalShift shift, ItemViewHolder holder, int position) {
        return
                shiftDetailAdapterHelper.bindViewHolder(shift, holder, position) ||
                payableDetailAdapterHelper.bindViewHolder(shift, holder, position) ||
                super.bindViewHolder(shift, holder, position);
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, PayableDetailAdapterHelper.Callbacks {
        void changeDate(long id, @NonNull ShiftData shiftData);
        void changeTime(long id, boolean isStart, @NonNull ShiftData shiftData);
    }

}
