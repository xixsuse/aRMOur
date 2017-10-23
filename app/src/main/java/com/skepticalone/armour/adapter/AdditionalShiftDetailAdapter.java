package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.AdditionalShift;

public final class AdditionalShiftDetailAdapter extends ItemDetailAdapter<AdditionalShift> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_SHIFT_TYPE = 3,
            ROW_NUMBER_HOURLY_RATE = 4,
            ROW_NUMBER_TOTAL_PAYMENT = 5,
            ROW_NUMBER_COMMENT = 6,
            ROW_NUMBER_CLAIMED = 7,
            ROW_NUMBER_PAID = 8,
            ROW_COUNT = 9;

    @NonNull
    private final ShiftDetailAdapterHelper<AdditionalShift> shiftDetailAdapterHelper;
    @NonNull
    private final PayableDetailAdapterHelper<AdditionalShift> payableDetailAdapterHelper;

    public AdditionalShiftDetailAdapter(@NonNull Context context, @NonNull final Callbacks callbacks) {
        super(context, callbacks);
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper<AdditionalShift>(callbacks) {
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
        payableDetailAdapterHelper = new PayableDetailAdapterHelper<AdditionalShift>(callbacks) {
            @Override
            int getRowNumberPayment() {
                return ROW_NUMBER_HOURLY_RATE;
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
            int getPaymentTitle() {
                return R.string.hourly_rate;
            }

            @Override
            int getPaymentIcon() {
                return R.drawable.ic_watch_black_24dp;
            }
        };
    }

    @Override
    int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getFixedRowCount() {
        return ROW_COUNT;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.hideSecondaryIcon();
        return holder;
    }

    @Override
    void notifyUpdated(@NonNull AdditionalShift oldShift, @NonNull AdditionalShift newShift) {
        super.notifyUpdated(oldShift, newShift);
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
        payableDetailAdapterHelper.onChanged(oldShift, newShift, this);
        if (!oldShift.getTotalPayment().equals(newShift.getTotalPayment())) {
            notifyItemChanged(ROW_NUMBER_TOTAL_PAYMENT);
        }
    }

    @Override
    void onBindViewHolder(@NonNull AdditionalShift shift, int position, @NonNull ItemViewHolder holder) {
        if (position == ROW_NUMBER_TOTAL_PAYMENT) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_dollar_black_24dp);
            holder.setText(getContext().getString(R.string.payment), getPaymentString(shift.getTotalPayment()));
            holder.itemView.setOnClickListener(null);
        } else if (!payableDetailAdapterHelper.bindViewHolder(shift, position, holder, this) && !shiftDetailAdapterHelper.bindViewHolder(shift, position, holder, this)) {
            super.onBindViewHolder(shift, position, holder);
        }
    }

    public interface Callbacks extends PayableDetailAdapterHelper.Callbacks, DateDetailAdapterHelper.Callbacks {
        void changeTime(boolean start);
    }

}
