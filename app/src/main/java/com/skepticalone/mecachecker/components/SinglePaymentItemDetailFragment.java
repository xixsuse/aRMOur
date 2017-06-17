package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.composition.AbstractData;
import com.skepticalone.mecachecker.composition.PaymentData;

abstract class SinglePaymentItemDetailFragment extends DetailFragment implements PaymentData.Callbacks {

    private final PaymentData mPaymentData = new PaymentData(this);

    abstract int getRowCountIfClaimed();

    abstract int getRowNumberPaidIfClaimed();

    @Override
    final int getRowCountIfLoaded() {
        int rowCount = getRowCountIfClaimed();
        if (!mPaymentData.isClaimed()) rowCount--;
        return rowCount;
    }

    @Override
    public final int getRowNumberPaid() {
        return mPaymentData.isClaimed() ? getRowNumberPaidIfClaimed() : NO_ROW_NUMBER;
    }

    @NonNull
    abstract AbstractData getData();

    @Override
    public final int getMoneyDescriptor() {
        return R.string.payment;
    }

    @Override
    public final int getMoneyIcon() {
        return R.drawable.ic_dollar_black_24dp;
    }

    @Override
    final void readFromPositionedCursor(@NonNull Cursor cursor) {
        mPaymentData.readFromPositionedCursor(cursor);
        getData().readFromPositionedCursor(cursor);
    }

    @Override
    boolean onBindListItemViewHolder(ListItemViewHolder holder, int position) {
        return mPaymentData.bindToHolder(getActivity(), holder, position) || getData().bindToHolder(getActivity(), holder, position);
    }

}
