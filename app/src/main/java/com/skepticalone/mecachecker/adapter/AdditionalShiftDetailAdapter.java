package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftDetailAdapter extends ItemDetailAdapter<AdditionalShiftEntity> {

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
    private final ShiftUtil.Calculator calculator;
    private final Callbacks callbacks;
    private final PayableDetailAdapterHelper payableDetailAdapterHelper;

    public AdditionalShiftDetailAdapter(Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks);
        this.callbacks = callbacks;
        this.calculator = calculator;
        payableDetailAdapterHelper = new PayableDetailAdapterHelper(callbacks) {
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
    int getRowCount(@NonNull AdditionalShiftEntity item) {
        return ROW_COUNT;
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    void onItemUpdated(@NonNull AdditionalShiftEntity oldShift, @NonNull AdditionalShiftEntity newShift) {
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
    boolean bindViewHolder(@NonNull AdditionalShiftEntity shift, ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_DATE) {
            holder.setupPlain(R.drawable.ic_calendar_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeDate();
                }
            });
            holder.setText(holder.getText(R.string.date), DateTimeUtils.getFullDateString(shift.getShiftData().getStart().toLocalDate()));
            return true;
        } else if (position == ROW_NUMBER_START) {
            holder.setupPlain(R.drawable.ic_play_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(true);
                }
            });
            holder.setText(holder.getText(R.string.start), DateTimeUtils.getStartTimeString(shift.getShiftData().getStart().toLocalTime()));
            return true;
        } else if (position == ROW_NUMBER_END) {
            holder.setupPlain(R.drawable.ic_stop_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(false);
                }
            });
            holder.setText(holder.getText(R.string.end), DateTimeUtils.getEndTimeString(shift.getShiftData().getEnd(), shift.getShiftData().getStart().toLocalDate()));
            return true;
        } else if (position == ROW_NUMBER_SHIFT_TYPE) {
            ShiftUtil.ShiftType shiftType = calculator.getShiftType(shift.getShiftData());
            holder.setupPlain(ShiftUtil.getShiftIcon(shiftType), null);
            holder.setText(holder.getText(ShiftUtil.getShiftTitle(shiftType)), DateTimeUtils.getPeriodString(shift.getShiftData().getDuration()));
            return true;
        } else return payableDetailAdapterHelper.bindViewHolder(shift, holder, position) || super.bindViewHolder(shift, holder, position);
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, PayableDetailAdapterHelper.Callbacks {
        void changeDate();
        void changeTime(boolean start);
    }

}
