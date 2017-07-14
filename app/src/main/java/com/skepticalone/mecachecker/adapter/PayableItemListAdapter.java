package com.skepticalone.mecachecker.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.model.PayableItem;
import com.skepticalone.mecachecker.util.Comparators;

abstract class PayableItemListAdapter<ItemType extends PayableItem> extends ItemListAdapter<ItemType> {

    PayableItemListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    @CallSuper
    boolean areContentsTheSame(@NonNull ItemType item1, @NonNull ItemType item2) {
        return super.areContentsTheSame(item1, item2) &&
                Comparators.equalPaymentData(item1.getPaymentData(), item2.getPaymentData());
    }

    @Override
    final void bindViewHolder(@NonNull ItemType item, ItemViewHolder holder) {
        holder.secondaryIcon.setImageResource(item.getPaymentData().getIcon());
        holder.setText(getViewHolderTitle(item), item.getComment());
    }

    abstract String getViewHolderTitle(@NonNull ItemType item);
}
