package com.skepticalone.armour.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Payment;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.ZonedDateTime;

abstract class PayableDetailAdapterHelper<Entity extends Payment> {

    @NonNull
    private final Callbacks callbacks;

    PayableDetailAdapterHelper(@NonNull Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @CallSuper
    void onItemUpdated(@NonNull Entity oldItem, @NonNull Entity newItem, @NonNull RecyclerView.Adapter adapter) {
        if (!oldItem.getPaymentData().getPayment().equals(newItem.getPaymentData().getPayment())) {
            adapter.notifyItemChanged(getRowNumberPayment());
        }
        if (!Comparators.equalDateTimes(oldItem.getPaymentData().getClaimed(), newItem.getPaymentData().getClaimed()) || !Comparators.equalDateTimes(oldItem.getPaymentData().getPaid(), newItem.getPaymentData().getPaid())) {
            adapter.notifyItemChanged(getRowNumberClaimed());
            adapter.notifyItemChanged(getRowNumberPaid());
        }
    }

    @CallSuper
    boolean bindViewHolder(@NonNull ContextAdapter adapter, @NonNull Entity item, ItemViewHolder holder, int position) {
        if (position == getRowNumberPayment()) {
            holder.setupPlain();
            holder.setPrimaryIcon(getPaymentIcon());
            holder.setText(adapter.getContext().getString(getPaymentTitle()), adapter.getPaymentString(item.getPaymentData().getPayment()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changePayment();
                }
            });
            return true;
        } else if (position == getRowNumberClaimed()) {
            holder.setupSwitch();
            ZonedDateTime claimed = item.getPaymentData().getClaimed();
            holder.bindSwitch(claimed != null, item.getPaymentData().getPaid() == null ? new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean claimed) {
                    callbacks.setClaimed(claimed);
                }
            } : null);
            if (claimed == null) {
                holder.setPrimaryIcon(0);
                holder.setText(adapter.getContext().getString(R.string.claimed));
            } else {
                holder.setPrimaryIcon(R.drawable.claimed_black_24dp);
                holder.setText(adapter.getContext().getString(R.string.claimed), DateTimeUtils.getDateTimeString(claimed.toLocalDateTime()));
            }
            return true;
        } else if (position == getRowNumberPaid()) {
            holder.setupSwitch();
            ZonedDateTime paid = item.getPaymentData().getPaid();
            holder.bindSwitch(paid != null, item.getPaymentData().getClaimed() == null ? null : new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean paid) {
                    callbacks.setPaid(paid);
                }
            });
            if (paid == null) {
                holder.setPrimaryIcon(0);
                holder.setText(adapter.getContext().getString(R.string.paid));
            } else {
                holder.setPrimaryIcon(R.drawable.paid_black_24dp);
                holder.setText(adapter.getContext().getString(R.string.paid), DateTimeUtils.getDateTimeString(paid.toLocalDateTime()));
            }
            return true;
        } else return false;
    }


    @StringRes
    int getPaymentTitle() {
        return R.string.payment;
    }

    @DrawableRes
    int getPaymentIcon() {
        return R.drawable.ic_dollar_black_24dp;
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
