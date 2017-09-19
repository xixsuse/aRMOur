package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.data.entity.CrossCoverEntity;

import org.threeten.bp.LocalDate;

public final class CrossCoverDetailAdapter extends PayableDetailAdapter<CrossCoverEntity> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    @NonNull
    private final DateDetailAdapterHelper<CrossCoverEntity> dateDetailAdapterHelper;

    public CrossCoverDetailAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
        dateDetailAdapterHelper = new DateDetailAdapterHelper<CrossCoverEntity>(callbacks) {
            @Override
            int getRowNumberDate() {
                return ROW_NUMBER_DATE;
            }

            @NonNull
            @Override
            LocalDate getDate(@NonNull CrossCoverEntity crossCover) {
                return crossCover.getDate();
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
    int getRowNumberComment(@NonNull CrossCoverEntity crossCover) {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getRowCount(@NonNull CrossCoverEntity crossCover) {
        return ROW_COUNT;
    }

    @Override
    public final ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    void onItemUpdated(@NonNull CrossCoverEntity oldCrossCover, @NonNull CrossCoverEntity newCrossCover) {
        super.onItemUpdated(oldCrossCover, newCrossCover);
        dateDetailAdapterHelper.onItemUpdated(oldCrossCover, newCrossCover, this);
    }

    @Override
    boolean bindViewHolder(@NonNull CrossCoverEntity crossCover, ItemViewHolder holder, int position) {
        return dateDetailAdapterHelper.bindViewHolder(crossCover, holder, position) ||
                super.bindViewHolder(crossCover, holder, position);
    }

    public interface Callbacks extends PayableDetailAdapter.Callbacks, DateDetailAdapterHelper.Callbacks {}

}
