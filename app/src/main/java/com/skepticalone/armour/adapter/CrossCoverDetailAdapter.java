package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.skepticalone.armour.data.model.CrossCover;

import org.threeten.bp.LocalDate;

public final class CrossCoverDetailAdapter extends ItemDetailAdapter<CrossCover> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    @NonNull
    private final PayableDetailAdapterHelper<CrossCover> payableDetailAdapterHelper;

    @NonNull
    private final DateDetailAdapterHelper<CrossCover> dateDetailAdapterHelper;

    public CrossCoverDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context, callbacks);
        payableDetailAdapterHelper = new PayableDetailAdapterHelper<CrossCover>(callbacks) {
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
        dateDetailAdapterHelper = new DateDetailAdapterHelper<CrossCover>(callbacks) {
            @Override
            int getRowNumberDate() {
                return ROW_NUMBER_DATE;
            }

            @NonNull
            @Override
            LocalDate getDate(@NonNull CrossCover crossCover) {
                return crossCover.getDate();
            }
        };
    }

    @Override
    int getRowNumberComment(@NonNull CrossCover crossCover) {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getRowCount(@NonNull CrossCover crossCover) {
        return ROW_COUNT;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.hideSecondaryIcon();
        return holder;
    }

    @Override
    void onItemUpdated(@NonNull CrossCover oldCrossCover, @NonNull CrossCover newCrossCover) {
        super.onItemUpdated(oldCrossCover, newCrossCover);
        payableDetailAdapterHelper.onItemUpdated(oldCrossCover, newCrossCover, this);
        dateDetailAdapterHelper.onItemUpdated(oldCrossCover, newCrossCover, this);
    }

    @Override
    boolean bindViewHolder(@NonNull CrossCover crossCover, ItemViewHolder holder, int position) {
        return dateDetailAdapterHelper.bindViewHolder(this, crossCover, holder, position) ||
                payableDetailAdapterHelper.bindViewHolder(this, crossCover, holder, position) ||
                super.bindViewHolder(crossCover, holder, position);
    }

    public interface Callbacks extends PayableDetailAdapterHelper.Callbacks, DateDetailAdapterHelper.Callbacks {
    }

}
