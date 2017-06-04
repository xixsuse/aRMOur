package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.View;

public abstract class AbstractSinglePaymentItemListFragment extends AbstractPaymentItemListFragment {

    abstract CharSequence getText(@NonNull Cursor cursor);

    @Override
    final void onViewHolderCreated(ListItemViewHolder holder) {
        holder.primaryIcon.setVisibility(View.GONE);
    }

    @Override
    final void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor) {
        super.bindViewHolderToCursor(holder, cursor);
        holder.text.setText(getText(cursor));
    }

}