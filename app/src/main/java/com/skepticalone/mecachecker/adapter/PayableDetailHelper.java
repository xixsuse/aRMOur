package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.Payable;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

final class PayableDetailHelper<ItemType extends Payable> {

    private final PayableDetailFragmentCallbacks mFragmentCallbacks;
    private final int rowNumberPayment, rowNumberClaimed, rowNumberPaid;

    PayableDetailHelper(
            PayableDetailFragmentCallbacks fragmentCallbacks,
            int rowNumberPayment,
            int rowNumberClaimed,
            int rowNumberPaid
    ) {
        mFragmentCallbacks = fragmentCallbacks;
        this.rowNumberPayment = rowNumberPayment;
        this.rowNumberClaimed = rowNumberClaimed;
        this.rowNumberPaid = rowNumberPaid;
    }

    void onItemUpdated(@NonNull Payable oldItem, @NonNull Payable newItem, RecyclerView.Adapter adapter) {
        if (!Comparators.equalBigDecimals(oldItem.getPaymentData().getPayment(), newItem.getPaymentData().getPayment())) {
            adapter.notifyItemChanged(rowNumberPayment);
        }
        if (!Comparators.equalDateTimes(oldItem.getPaymentData().getClaimed(), newItem.getPaymentData().getClaimed()) || !Comparators.equalDateTimes(oldItem.getPaymentData().getPaid(), newItem.getPaymentData().getPaid())) {
            adapter.notifyItemChanged(rowNumberClaimed);
            adapter.notifyItemChanged(rowNumberPaid);
        }
    }

    boolean bindViewHolder(@NonNull final ItemType item, ItemViewHolder holder, int position) {
        if (position == rowNumberPayment) {
            holder.setupPlain(R.drawable.ic_dollar_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFragmentCallbacks.changePayment(item.getId(), item.getPaymentData().getPayment());
                }
            });
            holder.setText(holder.getText(R.string.payment), holder.getText(R.string.currency_format, item.getPaymentData().getPayment()));
            return true;
        } else if (position == rowNumberClaimed) {
            CompoundButton.OnCheckedChangeListener onClaimedCheckedChangeListener = item.getPaymentData().getPaid() == null ? new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean claimed) {
                    mFragmentCallbacks.setClaimed(item.getId(), claimed);
                }
            } : null;
            DateTime claimed = item.getPaymentData().getClaimed();
            if (claimed == null) {
                holder.setupSwitch(0, false, onClaimedCheckedChangeListener);
                holder.setText(holder.getText(R.string.claimed));
            } else {
                holder.setupSwitch(R.drawable.ic_check_box_half_black_24dp, true, onClaimedCheckedChangeListener);
                holder.setText(holder.getText(R.string.claimed), DateTimeUtils.getDateTimeString(claimed));
            }
            return true;
        } else if (position == rowNumberPaid) {
            CompoundButton.OnCheckedChangeListener onPaidCheckedChangeListener = item.getPaymentData().getClaimed() == null ? null : new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean paid) {
                    mFragmentCallbacks.setPaid(item.getId(), paid);
                }
            };
            DateTime paid = item.getPaymentData().getPaid();
            if (paid == null) {
                holder.setupSwitch(0, false, onPaidCheckedChangeListener);
                holder.setText(holder.getText(R.string.paid));
            } else {
                holder.setupSwitch(R.drawable.ic_check_box_full_black_24dp, true, onPaidCheckedChangeListener);
                holder.setText(holder.getText(R.string.paid), DateTimeUtils.getDateTimeString(paid));
            }
            return true;
        } else return false;
    }

}
