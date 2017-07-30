package com.skepticalone.mecachecker.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Payable;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

public abstract class PayableDetailAdapter<Entity extends Payable> extends ItemDetailAdapter<Entity> {

    @NonNull
    private final Callbacks callbacks;

    PayableDetailAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
        this.callbacks = callbacks;
    }

    @Override
    @CallSuper
    void onItemUpdated(@NonNull Entity oldItem, @NonNull Entity newItem) {
        super.onItemUpdated(oldItem, newItem);
        if (!Comparators.equalBigDecimals(oldItem.getPaymentData().getPayment(), newItem.getPaymentData().getPayment())) {
            notifyItemChanged(getRowNumberPayment());
        }
        if (!Comparators.equalDateTimes(oldItem.getPaymentData().getClaimed(), newItem.getPaymentData().getClaimed()) || !Comparators.equalDateTimes(oldItem.getPaymentData().getPaid(), newItem.getPaymentData().getPaid())) {
            notifyItemChanged(getRowNumberClaimed());
            notifyItemChanged(getRowNumberPaid());
        }
    }

    @StringRes
    int getPaymentTitle() {
        return R.string.payment;
    }

    @DrawableRes
    int getPaymentIcon() {
        return R.drawable.ic_dollar_black_24dp;
    }

    @Override
    @CallSuper
    boolean bindViewHolder(@NonNull Entity item, ItemViewHolder holder, int position) {
        if (position == getRowNumberPayment()) {
            holder.setupPlain(getPaymentIcon(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changePayment();
                }
            });
            holder.setText(holder.getText(getPaymentTitle()), holder.getCurrencyText(item.getPaymentData().getPayment()));
            return true;
        } else if (position == getRowNumberClaimed()) {
            CompoundButton.OnCheckedChangeListener onClaimedCheckedChangeListener = item.getPaymentData().getPaid() == null ? new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean claimed) {
                    callbacks.setClaimed(claimed);
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
        } else if (position == getRowNumberPaid()) {
            CompoundButton.OnCheckedChangeListener onPaidCheckedChangeListener = item.getPaymentData().getClaimed() == null ? null : new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean paid) {
                    callbacks.setPaid(paid);
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
        } else return super.bindViewHolder(item, holder, position);
    }

    abstract int getRowNumberPayment();
    abstract int getRowNumberClaimed();
    abstract int getRowNumberPaid();

    public interface Callbacks extends ItemDetailAdapter.Callbacks {
        void changePayment();
        void setClaimed(boolean claimed);
        void setPaid(boolean paid);
    }
}
