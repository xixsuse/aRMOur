package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;

abstract class SinglePaymentItemDetailFragment extends DetailFragment implements PaymentData.Callbacks {

    private final PaymentData mPaymentData = new PaymentData(this);

    @NonNull
    abstract AbstractData getData();

    abstract int getRowCountIfPaid();

    @Override
    public final int getMoneyTitle() {
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
    final int getRowCountIfLoaded() {
        return mPaymentData.getAdjustedRowCount(getRowCountIfPaid());
    }

    @Override
    final boolean isSwitchType(int position) {
        return AbstractData.isSwitchType(position, mPaymentData, getData());
    }

    @Override
    final PlainListItemViewHolder onCreatePlainListItemViewHolder(ViewGroup parent) {
        PlainListItemViewHolder holder = super.onCreatePlainListItemViewHolder(parent);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    final SwitchListItemViewHolder onCreateSwitchListItemViewHolder(ViewGroup parent) {
        return new SwitchListItemViewHolder(parent, mPaymentData);
    }

    @Override
    final boolean bindPlainListItemViewHolder(PlainListItemViewHolder holder, int position) {
        return mPaymentData.bindToHolder(getActivity(), holder, position) || getData().bindToHolder(getActivity(), holder, position);
    }

    @Override
    final boolean bindSwitchListItemViewHolder(SwitchListItemViewHolder holder, int position) {
        return mPaymentData.bindToHolder(getActivity(), holder, position);
    }

}
