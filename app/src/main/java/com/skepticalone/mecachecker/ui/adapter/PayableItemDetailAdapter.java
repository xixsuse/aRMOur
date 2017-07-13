package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.PayableItem;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public abstract class PayableItemDetailAdapter<ItemType extends PayableItem> extends ItemDetailAdapter<ItemType> {

    private final Callbacks mCallbacks;

    PayableItemDetailAdapter(Callbacks callbacks) {
        super();
        mCallbacks = callbacks;
    }

    abstract int getRowNumberPayment();

    abstract int getRowNumberComment();

    abstract int getRowNumberClaimed();

    abstract int getRowNumberPaid();

    @Override
    @CallSuper
    void onItemUpdated(@NonNull ItemType oldItem, @NonNull ItemType newItem) {
        if (!Comparators.equalBigDecimals(oldItem.getPaymentData().getPayment(), newItem.getPaymentData().getPayment())) {
            notifyItemChanged(getRowNumberPayment());
        }
        if (!Comparators.equalStrings(oldItem.getPaymentData().getComment(), newItem.getPaymentData().getComment())) {
            notifyItemChanged(getRowNumberComment());
        }
        if (!Comparators.equalDateTimes(oldItem.getPaymentData().getClaimed(), newItem.getPaymentData().getClaimed()) || !Comparators.equalDateTimes(oldItem.getPaymentData().getPaid(), newItem.getPaymentData().getPaid())) {
            notifyItemChanged(getRowNumberClaimed());
            notifyItemChanged(getRowNumberPaid());
        }
    }

    @Override
    @CallSuper
    void bindViewHolder(@NonNull final ItemType item, ItemViewHolder holder, int position) {
        if (position == getRowNumberPayment()) {
            holder.setupPlain(R.drawable.ic_dollar_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbacks.changePayment(item.getId(), item.getPaymentData().getPayment());
                }
            });
            holder.setText(R.string.payment, R.string.currency_format, item.getPaymentData().getPayment());
        } else if (position == getRowNumberComment()) {
            holder.setupPlain(R.drawable.ic_comment_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbacks.changeComment(item.getId(), item.getPaymentData().getComment());
                }
            });
            holder.setText(R.string.comment, item.getPaymentData().getComment());
        } else if (position == getRowNumberClaimed()) {
            CompoundButton.OnCheckedChangeListener onClaimedCheckedChangeListener = item.getPaymentData().getPaid() == null ? new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean claimed) {
                    mCallbacks.setClaimed(item.getId(), claimed);
                }
            } : null;
            DateTime claimed = item.getPaymentData().getClaimed();
            if (claimed == null) {
                holder.setupSwitch(0, false, onClaimedCheckedChangeListener);
                holder.setText(R.string.claimed, R.string.not_applicable);
            } else {
                holder.setupSwitch(R.drawable.ic_check_box_half_black_24dp, true, onClaimedCheckedChangeListener);
                holder.setText(R.string.claimed, DateTimeUtils.getDateTimeString(claimed));
            }
        } else if (position == getRowNumberPaid()) {
            CompoundButton.OnCheckedChangeListener onPaidCheckedChangeListener = item.getPaymentData().getClaimed() == null ? null : new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean paid) {
                    mCallbacks.setPaid(item.getId(), paid);
                }
            };
            DateTime paid = item.getPaymentData().getPaid();
            if (paid == null) {
                holder.setupSwitch(0, false, onPaidCheckedChangeListener);
                holder.setText(R.string.paid, R.string.not_applicable);
            } else {
                holder.setupSwitch(R.drawable.ic_check_box_full_black_24dp, true, onPaidCheckedChangeListener);
                holder.setText(R.string.paid, DateTimeUtils.getDateTimeString(paid));
            }
        } else {
            throw new IllegalStateException();
        }
    }

    public interface Callbacks {
        void changePayment(long itemId, @NonNull BigDecimal payment);

        void changeComment(long itemId, @Nullable String currentComment);

        void setClaimed(long itemId, boolean claimed);

        void setPaid(long itemId, boolean paid);
    }
}
