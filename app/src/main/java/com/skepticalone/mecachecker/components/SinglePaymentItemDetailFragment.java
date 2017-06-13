package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;

abstract class SinglePaymentItemDetailFragment extends DetailFragment implements PaymentData.Callbacks {

    private final PaymentData mPaymentData = new PaymentData(this);

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

    @Nullable
    @Override
    final ViewHolderType getViewHolderType(int position) {
        return AbstractData.getViewHolderType(position, mPaymentData, getData());
    }

    @Override
    final PlainListItemViewHolder createPlainListItemViewHolder(ViewGroup parent) {
        PlainListItemViewHolder holder = super.createPlainListItemViewHolder(parent);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    final SwitchListItemViewHolder createSwitchListItemViewHolder(ViewGroup parent) {
        return new SwitchListItemViewHolder(parent, mPaymentData);
    }

    @Override
    final boolean bindPlainListItemViewHolder(PlainListItemViewHolder holder, int position) {
        return mPaymentData.bindToHolder(getActivity(), holder, position) || getData().bindToHolder(getActivity(), holder, position);
    }

    @Override
    final boolean bindSwitchListItemViewHolder(SwitchListItemViewHolder holder, int position) {
        return mPaymentData.bindToHolder(getActivity(), holder, position) || getData().bindToHolder(getActivity(), holder, position);
    }

}
