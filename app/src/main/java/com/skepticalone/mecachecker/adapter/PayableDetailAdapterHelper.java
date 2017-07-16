package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.PayableItem;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

import java.math.BigDecimal;

final class PayableDetailAdapterHelper {

    private final Callbacks callbacks;

    PayableDetailAdapterHelper(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    void onItemUpdated(@NonNull PayableItem oldItem, @NonNull PayableItem newItem, RecyclerView.Adapter adapter) {
        if (!Comparators.equalBigDecimals(oldItem.getPaymentData().getPayment(), newItem.getPaymentData().getPayment())) {
            adapter.notifyItemChanged(callbacks.getRowNumberPayment());
        }
        if (!Comparators.equalDateTimes(oldItem.getPaymentData().getClaimed(), newItem.getPaymentData().getClaimed()) || !Comparators.equalDateTimes(oldItem.getPaymentData().getPaid(), newItem.getPaymentData().getPaid())) {
            adapter.notifyItemChanged(callbacks.getRowNumberClaimed());
            adapter.notifyItemChanged(callbacks.getRowNumberPaid());
        }
    }

    boolean bindViewHolder(@NonNull final PayableItem item, ItemViewHolder holder, int position) {
        if (position == callbacks.getRowNumberPayment()) {
            holder.setupPlain(R.drawable.ic_dollar_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.onPaymentClicked(item.getId(), item.getPaymentData().getPayment());
                }
            });
            holder.setText(holder.getText(R.string.payment), holder.getText(R.string.currency_format, item.getPaymentData().getPayment()));
            return true;
        } else if (position == callbacks.getRowNumberClaimed()) {
            CompoundButton.OnCheckedChangeListener onClaimedCheckedChangeListener = item.getPaymentData().getPaid() == null ? new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean claimed) {
                    callbacks.onClaimedToggled(item.getId(), claimed);
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
        } else if (position == callbacks.getRowNumberPaid()) {
            CompoundButton.OnCheckedChangeListener onPaidCheckedChangeListener = item.getPaymentData().getClaimed() == null ? null : new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean paid) {
                    callbacks.onPaidToggled(item.getId(), paid);
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

    public interface Callbacks {
        int getRowNumberPayment();
        int getRowNumberClaimed();
        int getRowNumberPaid();
        void onPaymentClicked(long id, @NonNull BigDecimal payment);
        void onClaimedToggled(long id, boolean claimed);
        void onPaidToggled(long id, boolean paid);
    }
}
