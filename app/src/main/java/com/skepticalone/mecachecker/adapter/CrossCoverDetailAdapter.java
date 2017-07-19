package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.CrossCover;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.LocalDate;

public final class CrossCoverDetailAdapter extends ItemDetailAdapter<CrossCover> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private final Callbacks callbacks;
    private final PayableDetailAdapterHelper payableDetailAdapterHelper;

    public CrossCoverDetailAdapter(Callbacks callbacks) {
        super(callbacks);
        this.callbacks = callbacks;
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
    int getRowCount(@NonNull CrossCover item) {
        return ROW_COUNT;
    }


    @Override
    void onItemUpdated(@NonNull CrossCover oldCrossCover, @NonNull CrossCover newCrossCover) {
        super.onItemUpdated(oldCrossCover, newCrossCover);
        payableDetailAdapterHelper.onItemUpdated(oldCrossCover, newCrossCover, this);
        if (!Comparators.equalLocalDates(oldCrossCover.getDate(), newCrossCover.getDate())) {
            notifyItemChanged(ROW_NUMBER_DATE);
        }
    }

    @Override
    boolean bindViewHolder(@NonNull final CrossCover crossCover, ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_DATE) {
            holder.setupPlain(R.drawable.ic_calendar_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeDate(crossCover.getId(), crossCover.getDate());
                }
            });
            holder.setText(holder.getText(R.string.date), DateTimeUtils.getFullDateString(crossCover.getDate()));
            return true;
        } else return payableDetailAdapterHelper.bindViewHolder(crossCover, holder, position) || super.bindViewHolder(crossCover, holder, position);
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, PayableDetailAdapterHelper.Callbacks {
        void changeDate(long id, @NonNull LocalDate currentDate);
    }

}
